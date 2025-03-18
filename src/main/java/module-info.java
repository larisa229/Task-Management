module com.example.pt2025_30422_pasca_larisa_assignment_1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.pt2025_30422_pasca_larisa_assignment_1 to javafx.fxml;
    exports com.example.pt2025_30422_pasca_larisa_assignment_1;
    exports dataModel;
    opens dataModel to javafx.fxml;
    exports businessLogic;
    opens businessLogic to javafx.fxml;

    exports graphicalUserInterface;
    opens graphicalUserInterface to javafx.graphics;
}
