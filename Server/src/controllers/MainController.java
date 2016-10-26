package controllers;

import controllers.exceptions.NonexistentEntityException;
import db.AdminDB;
import db.BookDB;
import db.BookTagDB;
import db.BorrowDB;
import db.TagDB;
import db.TagSettingDB;
import db.UserAccountDB;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Admin;
import model.Book;
import model.Borrow;
import model.Tag;
import model.UserAccount;
import remote.RemoteProcs;
import rpc.HiRpc;

public class MainController {

    private static final MainController INSTANCE = new MainController();

    private final UserAccountDBJpaController userAccountDBJpaController;
    private final AdminDBJpaController adminDBJpaController;
    private final TagDBJpaController tagDBJpaController;
    private final BookDBJpaController bookDBJpaController;
    private final BorrowDBJpaController borrowDBJpaController;
    private final TagSettingDBJpaController tagSettingDBJpaController;
    private final BookTagDBJpaController bookTagDBJpaController;

    private MainController() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ServerPU");
        this.userAccountDBJpaController = new UserAccountDBJpaController(emf);
        this.adminDBJpaController = new AdminDBJpaController(emf);
        this.tagDBJpaController = new TagDBJpaController(emf);
        this.bookDBJpaController = new BookDBJpaController(emf);
        this.borrowDBJpaController = new BorrowDBJpaController(emf);
        this.tagSettingDBJpaController = new TagSettingDBJpaController(emf);
        this.bookTagDBJpaController = new BookTagDBJpaController(emf);
    }

    public void startServer(RemoteProcs server) throws IOException {
        HiRpc.start(server, RemoteProcs.PORT);
    }

    private <C> C model(Object fo, Class<C> tc) {
        if (fo == null) {
            return null;
        }

        try {
            Constructor<C> tcons = tc.getDeclaredConstructor();
            tcons.setAccessible(true);
            C to = tcons.newInstance();

            Class fc = fo.getClass();
            Field[] ffs = fc.getDeclaredFields();
            for (Field ff : ffs) {
                if (Modifier.isStatic(ff.getModifiers())) {
                    continue;
                }

                try {
                    Field tf = tc.getDeclaredField(ff.getName());

                    tf.setAccessible(true);
                    ff.setAccessible(true);

                    tf.set(to, ff.get(fo));
                } catch (NoSuchFieldException | SecurityException e) {
                }
            }

            return (C) to;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public UserAccount findUser(int id) {
        UserAccountDB t = this.userAccountDBJpaController.findUserAccountDB(id);
        return this.model(t, UserAccount.class);
    }

    public boolean createUserAccount(UserAccount ua) {
        try {
            UserAccountDB uadb = this.userAccountDBJpaController.findUserByUsername(ua.getUsername());
            if (uadb != null) {
                return false;
            }
            uadb = this.model(ua, UserAccountDB.class);

            byte[] bts = MessageDigest.getInstance("MD5").digest(ua.getPassword());
            uadb.setPassword(bts);

            this.userAccountDBJpaController.create(uadb);
            return true;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    public UserAccount logUser(String username, String password) {
        try {
            UserAccountDB acdb = this.userAccountDBJpaController.findUserByUsername(username);
            if (acdb == null) {
                return null;
            }

            byte[] bts = MessageDigest.getInstance("MD5").digest(password.getBytes());

            if (!Arrays.equals(bts, acdb.getPassword())) {
                return null;
            }

            acdb.setPassword(null);
            return this.model(acdb, UserAccount.class);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean editUserAccount(UserAccount u) {
        try {
            UserAccountDB bdb = this.model(u, UserAccountDB.class);

            byte[] bts = MessageDigest.getInstance("MD5").digest(bdb.getPassword());
            bdb.setPassword(bts);

            this.userAccountDBJpaController.edit(bdb);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void deleteUserAccount(int user) {
        try {
            this.userAccountDBJpaController.destroy(user);
        } catch (NonexistentEntityException ex) {
        }
    }

    public TreeSet<UserAccount> searchUser(String name) {
        List<UserAccountDB> all = this.userAccountDBJpaController.searchByName(name);
        TreeSet<UserAccount> res = new TreeSet<>();

        for (UserAccountDB r : all) {
            res.add(this.model(r, UserAccount.class));
        }

        return res;
    }

    public TreeSet<Tag> findTags() {
        List<TagDB> all = this.tagDBJpaController.findTagDBEntities();
        TreeSet<Tag> res = new TreeSet<>();
        for (TagDB t : all) {
            res.add(this.model(t, Tag.class));
        }
        return res;
    }

    public void changeTagsOfUser(int user, Collection<Tag> tags) {
        List<TagSettingDB> sets = this.tagSettingDBJpaController.findTagSettingsByUser(user);
        for (TagSettingDB set : sets) {
            try {
                this.tagSettingDBJpaController.destroy(set.getId());
            } catch (NonexistentEntityException ex) {
            }
        }

        for (Tag s : tags) {
            this.tagSettingDBJpaController.create(new TagSettingDB(null, user, s.getId()));
        }
    }

    public void tagBook(int book, Collection<Tag> tags) {
        List<BookTagDB> sets = this.bookTagDBJpaController.findBookTagByBook(book);
        for (BookTagDB s : sets) {
            try {
                this.bookTagDBJpaController.destroy(s.getId());
            } catch (NonexistentEntityException ex) {
            }
        }

        for (Tag t : tags) {
            this.bookTagDBJpaController.create(new BookTagDB(null, book, t.getId()));
        }
    }

    public boolean createAdmin(Admin a) {
        try {
            AdminDB adb = this.adminDBJpaController.findAdminByUsername(a.getUsername());
            if (adb != null) {
                return false;
            }

            adb = this.model(a, AdminDB.class);

            byte[] bts = MessageDigest.getInstance("MD5").digest(a.getPassword());
            adb.setPassword(bts);

            this.adminDBJpaController.create(adb);
            return true;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Admin logAdmin(String username, String password) {
        try {
            AdminDB acdb = this.adminDBJpaController.findAdminByUsername(username);
            if (acdb == null) {
                return null;
            }

            byte[] bts = MessageDigest.getInstance("MD5").digest(password.getBytes());

            if (!Arrays.equals(bts, acdb.getPassword())) {
                return null;
            }

            acdb.setPassword(null);
            return this.model(acdb, Admin.class);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Book findBook(int id) {
        BookDB bdb = this.bookDBJpaController.findBookDB(id);
        return this.model(bdb, Book.class);
    }

    public String findBookName(int book) {
        return this.bookDBJpaController.findBookName(book);
    }

    public int createBook(Book b) {
        BookDB bdb = this.model(b, BookDB.class);
        this.bookDBJpaController.create(bdb);
        return bdb.getId();
    }

    public boolean editBook(Book b) {
        try {
            BookDB bdb = this.model(b, BookDB.class);
            this.bookDBJpaController.edit(bdb);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteBook(int book) {
        try {
            this.bookDBJpaController.destroy(book);
            return true;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public TreeSet<Book> searchBooks(String title) {
        List<BookDB> all = this.bookDBJpaController.searchByTitle(title);
        TreeSet<Book> res = new TreeSet<>();

        for (BookDB r : all) {
            res.add(this.model(r, Book.class));
        }

        return res;
    }

    public TreeSet<Book> findBooksByUser(int user) {
        List<BookDB> all = this.bookDBJpaController.findBooksByUser(user);
        TreeSet<Book> res = new TreeSet<>();
        for (BookDB b : all) {
            res.add(this.model(b, Book.class));
        }
        return res;
    }

    public TreeSet<Book> findBooksByTag(int tag) {
        List<BookDB> all = this.bookDBJpaController.findBooksByTag(tag);
        TreeSet<Book> res = new TreeSet<>();
        for (BookDB b : all) {
            res.add(this.model(b, Book.class));
        }
        return res;
    }

    public TreeSet<Borrow> findBorrows() {
        TreeSet<Borrow> res = new TreeSet<>();
        List<BorrowDB> all = this.borrowDBJpaController.findBorrowDBEntities();
        for (BorrowDB b : all) {
            res.add(this.model(b, Borrow.class));
        }
        return res;
    }

    public void deleteBorrow(int borrow) {
        try {
            this.borrowDBJpaController.destroy(borrow);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBorrow(int user, int book) {
        try {
            BorrowDB b = this.borrowDBJpaController.findBorrowsByUserBook(user, book);
            if (b == null) {
                return;
            }
            this.borrowDBJpaController.destroy(b.getId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Borrow findBorrowByUserBook(int user, int book) {
        BorrowDB b = this.borrowDBJpaController.findBorrowsByUserBook(user, book);
        return this.model(b, Borrow.class);
    }

    public Borrow borrowBook(int user, int book) {
        if (this.countBorrowsByUser(user) >= 5) {
            return null;
        }

        BorrowDB bdb = this.borrowDBJpaController.findBorrowsByUserBook(user, book);
        if (bdb != null) {
            return null;
        }

        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, 15);
        Date exp = c.getTime();

        BorrowDB b = new BorrowDB(null, user, book, d, exp, false);
        this.borrowDBJpaController.create(b);

        return this.model(b, Borrow.class);
    }

    public int countBorrowsByBook(int book) {
        return this.borrowDBJpaController.countByBook(book);
    }

    public int countBorrowsByUser(int book) {
        return this.borrowDBJpaController.countByUser(book);
    }

    public Boolean extendBorrow(int borrow) {
        try {
            BorrowDB bdb = this.borrowDBJpaController.findBorrowDB(borrow);
            if (bdb == null) {
                return null;
            }

            if (bdb.getExtended()) {
                return false;
            }

            Date ex = bdb.getExpiration();

            Calendar c = Calendar.getInstance();
            c.setTime(ex);
            c.add(Calendar.DATE, 15);
            ex = c.getTime();

            bdb.setExpiration(ex);
            bdb.setExtended(true);

            this.borrowDBJpaController.edit(bdb);
            return true;
        } catch (Exception ex1) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex1);
        }
        return null;
    }

    public Boolean extendBorrow(int user, int book) {
        try {
            BorrowDB bdb = this.borrowDBJpaController.findBorrowsByUserBook(user, book);
            if (bdb == null) {
                return null;
            }

            if (bdb.getExtended()) {
                return false;
            }

            Date ex = bdb.getExpiration();

            Calendar c = Calendar.getInstance();
            c.setTime(ex);
            c.add(Calendar.DATE, 15);
            ex = c.getTime();

            bdb.setExpiration(ex);
            bdb.setExtended(true);

            this.borrowDBJpaController.edit(bdb);
            return true;
        } catch (Exception ex1) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex1);
        }
        return null;
    }

    @Deprecated
    public TreeSet<Book> generateNewsFeedOld(int user) {//nasty
        List<TagDB> tags = this.tagDBJpaController.findTagsByUser(user);

        TreeSet<Book> res = new TreeSet<>();
        for (TagDB t : tags) {
            List<BookDB> bbts = this.bookDBJpaController.findBooksByTag(t.getId());
            if (bbts == null) {
                continue;
            }

            Collections.sort(bbts, new Comparator<BookDB>() {
                @Override
                public int compare(BookDB o1, BookDB o2) {
                    return countBorrowsByBook(o1.getId()) - countBorrowsByBook(o2.getId());
                }
            });

            /*int len = Math.min(5, bbts.size());
            for (int i = 0; i < len; ++i) {
                res.add(this.model(bbts.get(i), Book.class));
            }*/
            int index = 0;
            for (BookDB b : bbts) {
                if (index >= 5) {
                    break;
                }
                Book mdb = this.model(b, Book.class);
                if (res.contains(mdb)) {
                    continue;
                }
                res.add(mdb);
                ++index;
            }
        }
        return res;
    }

    public ArrayList<Integer> generateNewsFeed(int user) {//less nasty
        List<TagDB> tags = this.tagDBJpaController.findTagsByUser(user);

        ArrayList<Integer> res = new ArrayList<>();
        for (TagDB t : tags) {
            List<Integer> bbts = this.bookDBJpaController.findBookIdsByTag(t.getId());
            if (bbts == null) {
                continue;
            }

            Collections.sort(bbts, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return countBorrowsByBook(o1) - countBorrowsByBook(o2);
                }
            });

            int index = 0;
            for (Integer b : bbts) {
                if (index >= 5) {
                    break;
                }
                if (res.contains(b)) {
                    continue;
                }
                res.add(b);
                ++index;
            }
        }

        return res;
    }

    public boolean createBorrow(Borrow b) {
        BorrowDB bdb = this.borrowDBJpaController.findBorrowsByUserBook(b.getUserAccount(), b.getBook());
        if (bdb != null) {
            return false;
        }
        bdb = this.model(b, BorrowDB.class);
        this.borrowDBJpaController.create(bdb);
        return true;
    }

    public ArrayList<Borrow> findBorrowsByUser(int user) {
        List<BorrowDB> all = this.borrowDBJpaController.findBorrowsByUser(user);
        ArrayList<Borrow> res = new ArrayList<>(all.size());
        for (BorrowDB b : all) {
            res.add(this.model(b, Borrow.class));
        }
        return res;
    }

    public TreeSet<Tag> findTagsByUser(int user) {
        List<TagDB> dbs = this.tagDBJpaController.findTagsByUser(user);
        TreeSet<Tag> res = new TreeSet<>();
        for (TagDB t : dbs) {
            res.add(this.model(t, Tag.class));
        }
        return res;
    }

    public TreeSet<Tag> findTagsByBook(int book) {
        List<TagDB> dbs = this.tagDBJpaController.findTagsByBook(book);
        TreeSet<Tag> res = new TreeSet<>();
        for (TagDB t : dbs) {
            res.add(this.model(t, Tag.class));
        }
        return res;
    }

    public byte[] findBookContent(int book) {
        try (FileInputStream in = new FileInputStream("" + book)) {
            byte[] res = new byte[in.available()];
            in.read(res);
            byte[] cr = new byte[res.length];
            for (int i = 0; i < res.length; i++) {
                cr[i] = res[i]++;
            }
            return cr;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static MainController getInstance() {
        return INSTANCE;
    }

}
