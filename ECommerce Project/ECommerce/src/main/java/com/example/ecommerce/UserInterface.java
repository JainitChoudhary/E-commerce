package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {

    GridPane loginPage;
    HBox headerBar;

    HBox footerBar;
    Button signInButton;
    Label welcomeLabel;

    VBox body;

    Customer loggedInCustomer;

    ProductList productList=new ProductList();
    VBox productPage;

    Button placeOrderButton =new Button("Place Order");



    ObservableList<Product>itemsInCart= FXCollections.observableArrayList();

   public   BorderPane createContent(){
       BorderPane root =new BorderPane();

        root.setPrefSize(800,600);

       // root.getChildren().add(loginPage);
       root.setTop(headerBar);
       // root.setCenter(loginPage);
       body =new VBox();
       body.setPadding(new Insets(10));
       body.setAlignment(Pos.CENTER);
       root.setCenter(body);
       productPage= productList.getAllProducts();
       body.getChildren().add(productPage);
       root.setBottom(footerBar);

        return  root;
    }

    public  UserInterface(){
         createLoginPage();
         createHeaderBar();
         createFooterBar();
    }

    private void createLoginPage(){
        Text userNameText= new Text("User Name");
        Text passwordText= new Text("Password");

        TextField userName = new TextField("angad@gmail.com");
        userName.setPromptText("Type your user name here");
        PasswordField password = new PasswordField();
        password.setText("abc123");
        password.setPromptText("Type your password here");
        Label massageLabel= new Label("Hi");

        Button loginButton = new Button("Login");

        loginPage= new GridPane();
       // loginPage.setStyle(" -fx-background-color: gery;");
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(userNameText, 0, 0); // Add userNameText
        loginPage.add(userName, 1, 0);
        loginPage.add(passwordText, 0, 1);
        loginPage.add(password, 1, 1);

        loginPage.add(massageLabel, 0,2);
        loginPage.add(loginButton, 1,2);


        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name =userName.getText();
                String pass=password.getText();
                Login login=new Login();

                loggedInCustomer=login.customerLogin(name,pass);
                if(loggedInCustomer!=null){
                    massageLabel.setText("Welcome "+ loggedInCustomer.getName());
                    welcomeLabel.setText("Welcome-"+loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);

                }
                else{
                    massageLabel.setText("Login Failed !! please give correct username and password");
                }

            }
        });

    }

    private  void  createHeaderBar(){

       Button homeButton = new Button();
        Image image= new Image("C:\\ECommerce Project\\ECommerce\\src\\img.jpg");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(80);
        homeButton.setGraphic(imageView);

       TextField searcBar= new TextField();
       searcBar.setPromptText("Search here");
       searcBar.setPrefWidth(280);

       Button searchButton = new Button(" Search");

       signInButton =new Button("Sign In");
       welcomeLabel =new Label();

       Button caraButton =new Button("Cart");

       Button orderButton = new Button("Orders");




       headerBar =new HBox();
   //    headerBar.setStyle("-fx-background-color:grey;");
       headerBar.setPadding(new Insets(10));
       headerBar.setSpacing(10);
       headerBar.setAlignment(Pos.CENTER);
       headerBar.getChildren().addAll(homeButton,searcBar,searchButton,signInButton,caraButton,orderButton);


       signInButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               body.getChildren().clear();
               body.getChildren().add(loginPage);

               headerBar.getChildren().remove(signInButton);
           }
       });

       caraButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               body.getChildren().clear();
               VBox prodPage = productList.getProductsInCart(itemsInCart);
               prodPage.setAlignment(Pos.CENTER);
               prodPage.setSpacing(10);
               prodPage.getChildren().add(placeOrderButton);
               body.getChildren().add(prodPage);
               footerBar.setVisible(false);// all cases need to handled for this
           }
       });

       placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               if(itemsInCart==null){
                   showDiolog("Please add some products in the cart to  place order!");
                   return;
               }
               if(loggedInCustomer==null){
                   showDiolog("Please login first to place order");
                   return;
               }
               int count =Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
               if(count!=0){
                   showDiolog("Order for  "+count+" products placed successfully!!");
               }
               else{
                   showDiolog("Order failed!!");
               }
           }
       });


       homeButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               body.getChildren().clear();
               body.getChildren().add(productPage);
               footerBar.setVisible(true);

               if(loggedInCustomer==null && headerBar.getChildren().indexOf(signInButton)==-1){

                       headerBar.getChildren().add(signInButton);

               }
           }
       });
    }




    private  void  createFooterBar(){

        Button buyNowButton = new Button("BuyNow");
        Button addToCartButton= new Button(" Add to Cart");


        footerBar =new HBox();
        //    headerBar.setStyle("-fx-background-color:grey;");
        footerBar.setPadding(new Insets(10));
        footerBar.setSpacing(10);
       footerBar.setAlignment(Pos.CENTER);
        footerBar.getChildren().addAll(buyNowButton,addToCartButton);


        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                 Product product = productList.getSelectedProduct();
                if(product==null){
                    showDiolog("Please select a product first to  place order!");
                    return;
                }
                if(loggedInCustomer==null){
                    showDiolog("Please login first to place order");
                    return;
                }
                boolean status = Order.placeOrder(loggedInCustomer,product);
                if(status==true){
                    showDiolog("Order placed successfully!!");
                }
                else{
                    showDiolog("Order failed!!");
                }
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product= productList.getSelectedProduct();
                if(product==null){
                    showDiolog("Please select a product first to add it to Cart!");
                    return;
                }
                itemsInCart.add(product);
                showDiolog("Selected Item has been added to the cart successfully.");

            }
        });

    }
    private void showDiolog(String massage){
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);

        alert.setContentText(massage);
        alert.setTitle("Massage");
        alert.showAndWait();
    }

}














