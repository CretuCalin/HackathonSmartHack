package visual.components;

import java.awt.BorderLayout;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Borrow;
import model.UserAccount;
import remote.RemoteProcs;

public class BorrowsPanel extends JPanel {

    private final RemoteProcs connection;
    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable table = new JTable(this.model);
    private final JScrollPane pane = new JScrollPane(this.table);
    private final JButton refresh = new JButton("Refresh");

    public BorrowsPanel(RemoteProcs connection) {
        super(new BorderLayout());
        this.connection = connection;
        this.configTheme();
        this.printInfo();

        this.refresh.addActionListener((e) -> this.printInfo());
    }

    private void configTheme() {
        super.add(this.refresh, BorderLayout.SOUTH);
        super.add(this.pane);
        this.model.addColumn("Book");
        this.model.addColumn("User");
        this.model.addColumn("Date");
        this.model.addColumn("Expiration");
        this.model.addColumn("Extended");
    }

    private void printInfo() {
        this.model.setRowCount(0);
        Set<Borrow> all = this.connection.findBorrows();
        for (Borrow b : all) {
            UserAccount ua = this.connection.findUser(b.getUserAccount());
            String bn = this.connection.findBookName(b.getBook());

            Object[] data = {bn, ua, b.getDate(), b.getExpiration(), b.isExtended()};
            this.model.addRow(data);
        }
    }
}
