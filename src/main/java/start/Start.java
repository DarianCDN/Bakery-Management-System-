package start;

import presentation.Controller;
import presentation.View;

public class Start {

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setVisible(true);
    }
}