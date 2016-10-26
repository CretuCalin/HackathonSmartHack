package remote;

import controllers.MainController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import model.Admin;
import model.Book;
import model.Borrow;
import model.Tag;
import model.UserAccount;

public class Server implements RemoteProcs {

    private final MainController mc = MainController.getInstance();

    @Override
    public UserAccount findUser(int id) {
        return mc.findUser(id);
    }

    @Override
    public boolean createUserAccount(UserAccount ua) {
        return mc.createUserAccount(ua);
    }

    @Override
    public boolean editUserAccount(UserAccount ua) {
        return mc.editUserAccount(ua);
    }

    @Override
    public void deleteUserAccount(int user) {
        mc.deleteUserAccount(user);
    }

    @Override
    public TreeSet<UserAccount> searchUser(String name) {
        return mc.searchUser(name);
    }

    @Override
    public UserAccount logUser(String username, String password) {
        return mc.logUser(username, password);
    }

    @Override
    public TreeSet<Tag> findTags() {
        return mc.findTags();
    }

    @Override
    public void changeTagsOfUser(int user, Collection<Tag> tags) {
        mc.changeTagsOfUser(user, tags);
    }

    @Override
    public void tagBook(int book, Collection<Tag> tags) {
        mc.tagBook(book, tags);
    }

    @Override
    public TreeSet<Tag> findTagsByUser(int user) {
        return mc.findTagsByUser(user);
    }

    @Override
    public TreeSet<Tag> findTagsByBook(int book) {
        return mc.findTagsByBook(book);
    }

    @Override
    public int createBook(Book b) {
        return mc.createBook(b);
    }

    @Override
    public boolean editBook(Book b) {
        return mc.editBook(b);
    }

    @Override
    public boolean deleteBook(int book) {
        return mc.deleteBook(book);
    }

    @Override
    public TreeSet<Book> searchBooks(String title) {
        return mc.searchBooks(title);
    }

    @Override
    public Book findBook(int id) {
        return MainController.getInstance().findBook(id);
    }

    @Override
    public String findBookName(int book) {
        return mc.findBookName(book);
    }

    @Override
    public TreeSet<Book> findBooksByUser(int id) {
        return mc.findBooksByUser(id);
    }

    @Override
    public ArrayList<Integer> generateNewsFeed(int user) {
        return mc.generateNewsFeed(user);
    }

    @Override
    public TreeSet<Borrow> findBorrows() {
        return mc.findBorrows();
    }

    @Override
    public void deleteBorrow(int borrow) {
        mc.deleteBorrow(borrow);
    }

    @Override
    public void deleteBorrow(int user, int book) {
        mc.deleteBorrow(user, book);
    }

    @Override
    public Borrow findBorrowByUserBook(int user, int book) {
        return mc.findBorrowByUserBook(user, book);
    }

    @Override
    public Borrow borrowBook(int user, int book) {
        return mc.borrowBook(user, book);
    }

    @Override
    public int countBorrowsByUser(int user) {
        return mc.countBorrowsByUser(user);
    }

    @Override
    public Boolean extendBorrow(int borrow) {
        return mc.extendBorrow(borrow);
    }

    @Override
    public Boolean extendBorrow(int user, int book) {
        return mc.extendBorrow(user, book);
    }

    @Override
    public Admin logAdmin(String username, String password) {
        return mc.logAdmin(username, password);
    }

    @Override
    public byte[] findBookContent(int book) {
        return mc.findBookContent(book);
    }
}
