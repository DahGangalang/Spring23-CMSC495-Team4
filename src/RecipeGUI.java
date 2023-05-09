import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.StringJoiner;

import javax.swing.table.DefaultTableModel;

public class RecipeGUI extends JFrame implements ActionListener {
  private JLabel recipeLabel, ingredientsLabel, instructionsLabel, searchLabel;
  private JTextField recipeTextField;
  private JTextArea instructionsTextArea;
  private JButton addButton, removeButton, saveButton, viewButton, searchButton;
  private JPanel ingredientPanel;
  private int ingredientCount = 0;
  private JTextField searchTextField;

  public RecipeGUI(Boolean runFromCmdline) {
    // create labels for the recipe, ingredients, and instructions
    recipeLabel = new JLabel("Recipe Name:");
    ingredientsLabel = new JLabel("Ingredients:");
    instructionsLabel = new JLabel("Instructions:");
    searchLabel = new JLabel("Search:");

    // create text field for recipe name and text area for instructions
    recipeTextField = new JTextField(20);
    instructionsTextArea = new JTextArea(10, 20);
    searchTextField = new JTextField(20);

    // create buttons for adding, removing, and saving recipes
    addButton = new JButton("Add Ingredient");
    removeButton = new JButton("Remove Ingredient");
    saveButton = new JButton("Save Recipe");
    viewButton = new JButton("View All Recipes");
    searchButton = new JButton("Search Recipe");

    // add action listeners to the buttons
    addButton.addActionListener(this);
    removeButton.addActionListener(this);
    saveButton.addActionListener(this);
    viewButton.addActionListener(this);
    searchButton.addActionListener(this);

    // create panel for ingredients
    ingredientPanel = new JPanel(new GridLayout(1, 4));
    addIngredientFields();

    // create panel for buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);
    buttonPanel.add(saveButton);
    buttonPanel.add(viewButton);
    buttonPanel.add(searchButton);

    // create main panel and add components
    JPanel mainPanel = new JPanel(new GridLayout(5, 1));
    mainPanel.add(recipeLabel);
    mainPanel.add(recipeTextField);
    mainPanel.add(ingredientsLabel);
    mainPanel.add(ingredientPanel);
    mainPanel.add(instructionsLabel);
    mainPanel.add(new JScrollPane(instructionsTextArea));
    mainPanel.add(searchLabel);
    mainPanel.add(searchTextField);
    mainPanel.add(buttonPanel);

    // set JFrame properties
    setTitle("Recipe Manager");
    setContentPane(mainPanel);

    //Should keep Cmdline side if bad exit
    if(runFromCmdline) {
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    else {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    pack();
    
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == addButton) {
      addIngredientFields();
    } else if (e.getSource() == removeButton) {
      removeIngredientFields();
    } else if (e.getSource() == saveButton) {
      saveRecipe();
    } else if (e.getSource() == viewButton) {
      viewAllRecipes();
    } else if (e.getSource() == searchButton) {
      searchRecipes(false, "");
    }
  }
  
  private void addIngredientFields() {
    if (ingredientCount < 10) {
      // create new fields for quantity, measurement type, and ingredient name
      JTextField qtyField = new JTextField(4);
      JComboBox < String > measurementCombo = new JComboBox < String > (new String[] {
        "Teaspoon",
        "Tablespoons",
        "Ounce",
        "Pound",
        "Cup",
        "Pint",
        "Quart",
        "Gallon",
        "Kilogram",
        "Gram",
        "Mililiter",
        "Liter",
        "Bottle",
        "Can",
        "Stick",
        "Dash",
        "Pinch",
        ""
      });
      JTextField ingredientField = new JTextField(20);

      // add fields to the ingredient panel
      ingredientPanel.add(qtyField);
      ingredientPanel.add(measurementCombo);
      ingredientPanel.add(ingredientField);

      // increment ingredient count
      ingredientCount++;

      // redraw JFrame to show new fields
      revalidate();
      repaint();
    }
  }

  private void removeIngredientFields() {
    if (ingredientCount > 1) {
      // remove the last
      // three fields from the ingredient panel
      ingredientPanel.remove(ingredientPanel.getComponentCount() - 1);
      ingredientPanel.remove(ingredientPanel.getComponentCount() - 1);
      ingredientPanel.remove(ingredientPanel.getComponentCount() - 1);

      // decrement ingredient count
      ingredientCount--;

      // redraw JFrame to show removed fields
      revalidate();
      repaint();
    }
  }

  
 private void saveRecipe() {
    try {
        // open SQLite database connection
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:recipes.db");

        // create SQL statement for inserting recipe
        String insertSql = "INSERT INTO recipes (name, ingredients, instructions) VALUES (?, ?, ?)";

        // create SQL statement for inserting ingredient
        String insertIngSql = "INSERT INTO ingredients (recipe_id, qty, measurement, ingredient) VALUES (?, ?, ?, ?)";

        // create SQL statement for selecting recipe by name
        String selectSql = "SELECT id FROM recipes WHERE name = ?";

        // prepare statement for inserting recipe
        PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

        // set values for recipe name and instructions
        insertStmt.setString(1, recipeTextField.getText());
        insertStmt.setString(2, getIngredientsString());
        insertStmt.setString(3, instructionsTextArea.getText());

        // execute insert statement and get the generated key for the new recipe
        int numRows = insertStmt.executeUpdate();
        ResultSet rs = insertStmt.getGeneratedKeys();
        int recipeId = -1;
        if (rs.next()) {
            recipeId = rs.getInt(1);
        }

        // prepare statement for inserting ingredient
        PreparedStatement insertIngStmt = conn.prepareStatement(insertIngSql);

        // iterate through ingredient fields and insert each one
        for (Component c : ingredientPanel.getComponents()) {
            if (c instanceof JPanel) {
                JPanel ingredientPanel = (JPanel) c;
                // get quantity, measurement, and ingredient fields for current ingredient
                JTextField qtyField = (JTextField) ingredientPanel.getComponent(0);
                JComboBox<String> measurementCombo = (JComboBox<String>) ingredientPanel.getComponent(1);
                JTextField ingredientField = (JTextField) ingredientPanel.getComponent(2);

                // set values for ingredient fields and execute insert statement
                insertIngStmt.setInt(1, recipeId);
                insertIngStmt.setString(2, qtyField.getText());
                insertIngStmt.setString(3, measurementCombo.getSelectedItem().toString());
                insertIngStmt.setString(4, ingredientField.getText());
                insertIngStmt.executeUpdate();
            }
        }

        // close database connection and reset GUI fields
        conn.close();
        recipeTextField.setText("");
        instructionsTextArea.setText("");
        ingredientPanel.removeAll();
        addIngredientFields();

        // show success message
        JOptionPane.showMessageDialog(this, "Recipe saved successfully.");

    } catch (Exception ex) {
        // show error message if recipe could not be saved
        JOptionPane.showMessageDialog(this, "Error saving recipe: " + ex.getMessage());
        System.out.println("Error saving recipe: " + ex.getMessage());
    }
}

  private String getIngredientsString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ingredientCount; i++) {
      JTextField qtyField = (JTextField) ingredientPanel.getComponent(i * 3);
      JComboBox < String > measurementCombo = (JComboBox < String > ) ingredientPanel.getComponent(i * 3 + 1);
      JTextField ingredientField = (JTextField) ingredientPanel.getComponent(i * 3 + 2);
      sb.append(qtyField.getText() + " " + measurementCombo.getSelectedItem().toString() + " " + ingredientField.getText());
      sb.append("\n");
    }
    return sb.toString();
  }

private void viewAllRecipes() {
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
            int id = rs.getInt("id");
            String name = rs.getString("name");
            model.addRow(new Object[]{id, name});
        }

        // create table with the table model
        JTable table = new JTable(model);

        // add mouse listener to table
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int recipeId = (int) target.getValueAt(row, 0);
                    viewRecipe(recipeId, false);
                }
            }
        });
        

        // display table in a scroll pane
        JOptionPane.showMessageDialog(this, new JScrollPane(table), "View All Recipes", JOptionPane.PLAIN_MESSAGE);

        // close database connection
        conn.close();

    } catch (Exception ex) {
      // show error message if recipes could not be retrieved
      JOptionPane.showMessageDialog(this, "Error retrieving recipes: " + ex.getMessage());
    }
  }

  public void searchRecipes(boolean runFromCmdline, String cmdLineInput) {

    //Necessary Variables
    int recipe_id = 0;
    String textToSearchFor;
    
    //Build String to search for
    if(runFromCmdline) {
      textToSearchFor = cmdLineInput;
    }
    else {
      textToSearchFor = searchTextField.getText();
    }

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
      JOptionPane.showMessageDialog(this, "Error retrieving recipes: " + ex.getMessage());
    }
  }
  
  private void viewRecipe(int recipeId, boolean runFromCmdline) {
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
            else {
              // display recipe in a dialog box
              JOptionPane.showMessageDialog(this, "Recipe Name: " + name + "\n\n" +
                                                  "Ingredients:\n" + ingredients + "\n\n" +
                                                  "Instructions:\n" + instructions,
                                                  "View Recipe", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // display error message if recipe does not exist
            JOptionPane.showMessageDialog(this, "Recipe not found.", "View Recipe", JOptionPane.ERROR_MESSAGE);
        }

        // close database connection
        conn.close();

    } catch (Exception ex) {
        // display error message if an exception occurs
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "View Recipe", JOptionPane.ERROR_MESSAGE);
    }
  }
  public static void main(String[] args) throws ClassNotFoundException {
    
    Recipe_Tools.validateDatabase();

    SwingUtilities.invokeLater(() -> {
        RecipeGUI gui = new RecipeGUI(false);
        gui.setVisible(true);
        }
    );
  }
}