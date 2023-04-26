import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Add_Recipe_GUI extends JFrame {
    private JPanel mainPanel, ingredientsPanel, instructionsPanel;
    private JLabel recipeNameLabel, qtyLabel, measurementLabel, ingredientLabel, instructionsLabel;
    private JTextField recipeNameField;
    private JButton addIngredientBtn, submitBtn;
    private JTextArea instructionsArea;
    private JScrollPane instructionsScrollPane;

    private String[] measurementTypes = {"", "tsp", "tbsp", "cup", "oz", "lb"};

    public Add_Recipe_GUI() {
        super("Recipe Input");

        // create panels
        mainPanel = new JPanel(new GridLayout(3, 1));
        ingredientsPanel = new JPanel(new GridLayout(0, 4));
        instructionsPanel = new JPanel(new BorderLayout());

        // create labels
        recipeNameLabel = new JLabel("Recipe Name:");
        qtyLabel = new JLabel("Qty:");
        measurementLabel = new JLabel("Measurement:");
        ingredientLabel = new JLabel("Ingredient:");
        instructionsLabel = new JLabel("Instructions:");

        // create text fields and area
        recipeNameField = new JTextField();
        instructionsArea = new JTextArea(10, 50);
        instructionsScrollPane = new JScrollPane(instructionsArea);

        // create buttons
        addIngredientBtn = new JButton("+");
        submitBtn = new JButton("Submit");

        // add components to panels
        mainPanel.add(recipeNameLabel);
        mainPanel.add(recipeNameField);
        mainPanel.add(ingredientsPanel);
        mainPanel.add(addIngredientBtn);
        mainPanel.add(instructionsPanel);
        mainPanel.add(submitBtn);

        instructionsPanel.add(instructionsLabel, BorderLayout.NORTH);
        instructionsPanel.add(instructionsScrollPane, BorderLayout.CENTER);

        ingredientsPanel.add(qtyLabel);
        ingredientsPanel.add(measurementLabel);
        ingredientsPanel.add(ingredientLabel);
        ingredientsPanel.add(new JLabel()); // dummy label for spacing

        // add action listener for add ingredient button
        addIngredientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIngredientRow();
            }
        });

        // add action listener for submit button
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRecipe();
            }
        });

        // set JFrame properties
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
    }

    private void addIngredientRow() {
        JTextField qtyField = new JTextField();
        JComboBox<String> measurementBox = new JComboBox<>(measurementTypes);
        JTextField ingredientField = new JTextField();
        JButton removeBtn = new JButton("-");

        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingredientsPanel.remove(qtyField);
                ingredientsPanel.remove(measurementBox);
                ingredientsPanel.remove(ingredientField);
                ingredientsPanel.remove(removeBtn);
                ingredientsPanel.revalidate();
                ingredientsPanel.repaint();
            }
        });

        ingredientsPanel.add(qtyField);
        ingredientsPanel.add(measurementBox);
        ingredientsPanel.add(ingredientField);
        ingredientsPanel.add(removeBtn);
        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
    }

    private void submitRecipe() {
        String recipeName = recipeNameField.getText();
        String instructions = instructionsArea.getText();
        Component[] ingredientComps = ingredientsPanel.getComponents();
    String[] ingredients = new String[ingredientComps.length / 4];
    int index = 0;

    for (int i = 0; i < ingredientComps.length; i += 4) {
        JTextField qtyField = (JTextField) ingredientComps[i];
        JComboBox<String> measurementBox = (JComboBox<String>) ingredientComps[i + 1];
        JTextField ingredientField = (JTextField) ingredientComps[i + 2];
        String qty = qtyField.getText();
        String measurement = (String) measurementBox.getSelectedItem();
        String ingredient = ingredientField.getText();
        String ingredientStr = qty + " " + measurement + " " + ingredient;
        ingredients[index] = ingredientStr;
        index++;
    }

    // store the recipe data in an arrayList to send to the toolbox to be further processed
    ArrayList<String> recipeOutput = new ArrayList<>();
    recipeOutput.add("***Section: Header");
    recipeOutput.add("Title: " + recipeName);
    //TODO: Implement Author Name input
    recipeOutput.add("Author: User");
    recipeOutput.add("***Section: Ingredients");
    for (int i = 0; i < ingredients.length; i++) {
        recipeOutput.add(ingredients[i]);
    }
    recipeOutput.add("***Section: Instructions");
    recipeOutput.add(instructions);
    Recipe_Tools.writeGuiInputToFile(recipeOutput);

    // print out the recipe data (just for demonstration purposes)
    for (String data : recipeOutput) {
        System.out.println(data);
    }
}

public static void main(String[] args) {
    new Add_Recipe_GUI();
}}
