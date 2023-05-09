/*
 * Toolbox for some minor functions used throughout the recipe repoistory 
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringJoiner;

import javax.swing.table.DefaultTableModel;

public class Recipe_Tools {

    private final static String DATABASEFILENAME = "jdbc:sqlite:recipes.db";
    private final static String TEMPFILENAME = "/Recipe-Files/tempRecipeFile.txt";
    
    //For stripping out whitespace surrounding a string
    public static String stripSurroundingWhiteSpace(String input) {

        if(input == null) {
            return "";
        } //End If

        int start = 0;
        int end = input.length() - 1;
        while(start < end && Character.isWhitespace(input.charAt(start))) {
            start++;
        } //Enf While Loop

        while(end > start && Character.isWhitespace(input.charAt(end))) {
            end--;
        } //End While Loop

        return input.substring(start, end + 1);
    } //End of stripSurroundingWhiteSpace

    public static File getTempFile() {
        
        String currentDirectory = System.getProperty("user.dir");
        return new File(currentDirectory + TEMPFILENAME);
    } //End of getTempFile

    public static void writeGuiInputToFile(ArrayList<String> input) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getTempFile(), false));
            for(String s : input) {
                writer.write(s);
                writer.newLine();
            } //End of For
            writer.close();
        } catch(Exception e) {
            System.out.println("Something went wrong adding recipe from GUI!\n");
            e.printStackTrace();
        } //End of Try/Catch
    } //End of writeGuiInputToFile

    public static boolean hasMeasurement(String word) {
        
        //converts input to lowercase and removes any punctuation of easier switching
        switch(word.toLowerCase().replaceAll("\\p{Punct}","")) {
            case "lbs":
            case "pound":
            case "pounds":
            case "kilo":
            case "kilogram":
            case "kilograms":
            case "kg":
            case "g":
            case "gram":
            case "grams":
            case "miligram":
            case "miligrams":
            case "mililitre":
            case "mililitres":
            case "mililiter":
            case "mililiters":
            case "liter":
            case "litre":
            case "liters":
            case "litres":
            case "l":
            case "cup":
            case "cups":
            case "c":
            case "pint":
            case "pints":
            case "quart":
            case "quarts":
            case "qt":
            case "gallon":
            case "gallons":
            case "gal":
            case "tsp":
            case "teaspoon":
            case "teaspoons":
            case "tbsp":
            case "tablspoon":
            case "tablspoons":
            case "ounce":
            case "oz":
            case "ounces":
            case "bottle":
            case "bottles":
            case "can":
            case "cans":
            case "stick":
            case "sticks":
            case "dash":
            case "dashes":
            case "loaf":
            case "loaves":
            case "pinch":
            case "pinches":
            case "dram":
            case "drams":
                return true;

        } //End of Case

        return false;

    } //End of hasMeasurement
   
    //Tries to connect to database or start a new if non-existant
    public static void validateDatabase() {

        try {

            //Some basic variables for the database
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASEFILENAME);
            String tableName = "recipes";

            //Check if Table exists
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rule = meta.getTables(null, null, tableName, null);

            //If the Table doesn't exist, create it
            if(rule.next() == false) {
                //Recipes table
                Statement createRecipesStmt = conn.createStatement();
                String createRecipesTableSql = "CREATE TABLE recipes (id INTEGER PRIMARY KEY, name TEXT, ingredients TEXT, instructions TEXT, created_at TEXT)";
                createRecipesStmt.executeUpdate(createRecipesTableSql);

                //If Recipes doesn't exist, seems fair that we'd need to make ingredients table too
                //Ingredients table
                Statement createIngredientsStmt = conn.createStatement();
                String createIngredientsTableSql = "CREATE TABLE ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER, qty TEXT, measurement TEXT, ingredient TEXT, FOREIGN KEY(recipe_id) REFERENCES recipes(id))";
                createIngredientsStmt.executeUpdate(createIngredientsTableSql);
            }

            //Be sure to close connections
            conn.close();

        } catch(Exception e) {
            System.out.println("Error in Recipe_Tools.validateDatabase():\n");
            e.printStackTrace();
        } //End Try/Catch
    } //End of validateDatabase

    //Adds recipe to the SQL Database
    public static void addRecipeFromFileToDatabase(Recipe recipe) {

        //Pre-allocate some necessary variables
        StringBuilder ingredientStringBuilder;
        int recipeId;
        ArrayList<Ingredient> ingredients = recipe.getIngredients();

        //Access Database
        try {

            //Open SQLite database connection
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASEFILENAME);

            //Create SQL statement for inserting recipe and ingredients
            String insertSql = "INSERT INTO recipes (name, ingredients, instructions) VALUES (?, ?, ?)";
            String insertIngSql = "INSERT INTO ingredients (recipe_id, qty, measurement, ingredient) VALUES (?, ?, ?, ?)";
            //String selectSql = "SELECT id FROM recipes WHERE name = ?";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

            //Set Values to be inserted in the database
            insertStmt.setString(1, recipe.getTitle());
            ingredientStringBuilder = new StringBuilder();
            for(Ingredient ingredient : ingredients) {
                ingredientStringBuilder.append(ingredient.toString());
                ingredientStringBuilder.append("\n");
            } //End of For
            insertStmt.setString(2, ingredientStringBuilder.toString());
            ingredientStringBuilder = null;
            insertStmt.setString(3, recipe.getInstructions());

            //Set the UID for the new recipe
            ResultSet rs = insertStmt.getGeneratedKeys();
            if(rs.next()) {
                recipeId = rs.getInt(1);
            } //End of IF
            else {
                recipeId = new Random(System.currentTimeMillis()).nextInt();
            } //End of Else

            // prepare statement for inserting ingredient
            PreparedStatement insertIngStmt = conn.prepareStatement(insertIngSql);

            //Iterate through ingredient to insert each
            for(Ingredient ingredient : ingredients) {
                insertIngStmt.setInt(1, recipeId);
                insertIngStmt.setString(2, ingredient.getQuantity());
                insertIngStmt.setString(3, ingredient.getMeasurement());
                insertIngStmt.setString(4, ingredient.getIngredientName());
                insertIngStmt.executeUpdate();
            } //End of For

            //Success Message
            System.out.println("Successfully Added Recipe to Database");

        } catch(Exception e) {
            System.out.println("Error saving recipe: " + e.getMessage());
            e.printStackTrace();
        } //End of Try/Catch

    } //End of addRecipeToDatabase

    //To the Programming Gods, I beg forgiveness for the sloppiness of this copy-paste job
    //Function was originally written for a GUI usage (see RecipeGUI)
    //Trimmed for Command line usage
    //Search and retrieve recipe functions
    public static void searchRecipes(boolean runFromCmdline, String textToSearchFor) {

        //Necessary Variables
        int recipe_id = 0;
    
        try {
          // open SQLite database connection
          Class.forName("org.sqlite.JDBC");
          Connection conn = DriverManager.getConnection("jdbc:sqlite:recipes.db");
    
          // create SQL statement for selecting recipes by name
          String selectSql = "SELECT * FROM recipes WHERE name LIKE ? ORDER BY name";
    
          // prepare statement with search string and execute query
          PreparedStatement selectStmt = conn.prepareStatement(selectSql);
          selectStmt.setString(1, "%" + textToSearchFor + "%");
          ResultSet rs = selectStmt.executeQuery();
    
          // build table model from result set
          DefaultTableModel model = new DefaultTableModel(new Object[] {
            "ID",
            "Name"
          }, 0);
          while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            model.addRow(new Object[] {
              id,
              name
            });
            recipe_id = id;
          }
    
          // close database connection
          conn.close();
    
          // display result set in a JTable
          //JTable table = new JTable(model);
          //JOptionPane.showMessageDialog(this, new JScrollPane(table));
          viewRecipe(recipe_id, runFromCmdline);
    
        } catch (Exception ex) {

          // show error message if recipes could not be retrieved
         System.out.println("Error retrieving recipes:\n" + ex.getMessage());
        }
    } //End of searchRecipe

    //To the Programming Gods, I beg forgiveness for the sloppiness of this copy-paste job
    //Function was originally written for a GUI usage (see RecipeGUI)
    //Trimmed for Command line usage
    public static void viewRecipe(int recipeId, boolean runFromCmdline) {
        try {
            // open SQLite database connection
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:recipes.db");
    
            // create SQL statement for selecting recipe by ID
            String selectSql = "SELECT name, ingredients, instructions FROM recipes WHERE id = ?";
    
            // prepare statement for selecting recipe
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, recipeId);
    
            // execute select statement
            ResultSet rs = selectStmt.executeQuery();
    
            // check if recipe exists
            if (rs.next()) {
                // get recipe name, ingredients, and instructions
                String name = rs.getString("name");
                String ingredients = rs.getString("ingredients");
                String instructions = rs.getString("instructions");
    
                if(runFromCmdline) {
                  //Displays output to the command line
                  StringJoiner sj = new StringJoiner("\n");
                  sj.add("Title");
                  sj.add(name + "\n");
                  sj.add("Ingredients");
                  sj.add(ingredients + "\n");
                  sj.add("Instructions:");
                  sj.add(instructions);
                  sj.add("\n");
                  System.out.println(sj.toString());
                }
            }
    
            // close database connection
            conn.close();
    
        } catch (Exception ex) {
            // display error message if an exception occurs
            System.out.println("Error Viewing recipe:\n" + ex.getMessage());
        }
    } //End of viewRecipe

    public static void viewAllRecipes() {

        ArrayList<String> tableData = new ArrayList<>();

        try {
            // open SQLite database connection
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:recipes.db");
    
            // create SQL statement for selecting all recipes
            String selectSql = "SELECT id, name FROM recipes";
    
            // prepare statement for selecting all recipes
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
    
            // execute select statement
            ResultSet rs = selectStmt.executeQuery();
    
            // create table model and add columns
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Name");
    
            // add rows to table model
            while (rs.next()) {
                tableData.add(rs.getString("id"));
                tableData.add(rs.getString("name"));
            }

            //Display Table
            StringJoiner rows;
            StringBuilder tableOutput = new StringBuilder();
            String rowDivider = "-";
            for(int i = 0; i < tableData.size(); i++) {
                rows = new StringJoiner(" | ", " | "," |\n");
                rows.add(tableData.get(i));
                rows.add(tableData.get(++i));
                tableOutput.append(rows.toString() + "\n");
                for(int j = 0; j < rows.length(); j++) {
                    tableOutput.append(rowDivider);
                }
            }
            tableOutput.toString();



            // close database connection
            conn.close();
    
        } catch (Exception ex) {
            // show error message if recipes could not be retrieved
            System.out.println("Error Viewing recipe:\n" + ex.getMessage());
        }
    }
} //End of Recipe_Tools
