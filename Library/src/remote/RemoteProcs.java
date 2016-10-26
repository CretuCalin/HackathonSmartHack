package remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import model.Admin;
import model.Book;
import model.Borrow;
import model.Tag;
import model.UserAccount;

public interface RemoteProcs {

    int PORT = 6207;

    UserAccount findUser(int id);
    
    boolean createUserAccount(UserAccount ua);

    boolean editUserAccount(UserAccount ua);

    void deleteUserAccount(int user);

    UserAccount logUser(String username, String password);

    TreeSet<UserAccount> searchUser(String name);

    TreeSet<Tag> findTags();

    TreeSet<Tag> findTagsByUser(int user);

    TreeSet<Tag> findTagsByBook(int book);

    void changeTagsOfUser(int user, Collection<Tag> tags);

    Book findBook(int book);
    
    String findBookName(int book);

    TreeSet<Book> findBooksByUser(int user);//

    void tagBook(int book, Collection<Tag> tags);

    int createBook(Book b);

    boolean editBook(Book b);

    boolean deleteBook(int book);

    TreeSet<Book> searchBooks(String title);

    ArrayList<Integer> generateNewsFeed(int user);

    TreeSet<Borrow> findBorrows();
    
    void deleteBorrow(int borrow);

    void deleteBorrow(int user, int book);

    Borrow findBorrowByUserBook(int user, int book);
    
    Borrow borrowBook(int user, int book);

    int countBorrowsByUser(int user);

    Boolean extendBorrow(int borrow);

    Boolean extendBorrow(int user, int book);

    Admin logAdmin(String username, String password);
    
    byte[] findBookContent(int book);
}
