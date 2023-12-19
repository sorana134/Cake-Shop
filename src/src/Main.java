import domain.Cake;
import domain.Orders;
import gui.CakeController;
import gui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hsqldb.SchemaObjectSet;
import repository.* ;
import repository.DBOrderRepo;
import repository.DBRepository;
import service.Service;
import ui.MainUi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DBRepository repo = new DBCakeRepo();
        DBRepository repo1 = new DBOrderRepo();


        Service service = new Service(repo, repo1);
        MainController controller= new MainController(service);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainPage.fxml"));
        loader.setController(controller);


        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) throws ParseException {


        launch(args);
    }
}
