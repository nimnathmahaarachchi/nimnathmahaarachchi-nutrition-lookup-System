package nutrition

import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.geometry.Insets
import scalafx.stage.{Modality, Stage}
import scalafx.scene.Scene
import scalafx.event.ActionEvent
import scalafx.Includes._

class AddFoodDialog(parentStage: Stage, database: NutritionDatabase) {
  
  private val dialog = new Stage {
    title = "Add New Food"
    initModality(Modality.WindowModal)
    initOwner(parentStage)
    width = 400
    height = 500
  }
  
  private val nameField = new TextField { promptText = "Food name" }
  private val caloriesField = new TextField { promptText = "Calories per serving" }
  private val proteinField = new TextField { promptText = "Protein (g)" }
  private val carbsField = new TextField { promptText = "Carbohydrates (g)" }
  private val fatField = new TextField { promptText = "Fat (g)" }
  private val categoryCombo = new ComboBox[String] {
    items = scalafx.collections.ObservableBuffer("Fruit", "Vegetable", "Grain")
    value = "Fruit"
  }
  
  // Category-specific fields
  private val vitaminCField = new TextField { promptText = "Vitamin C (mg)" }
  private val fiberField = new TextField { promptText = "Fiber (g)" }
  private val vitaminAField = new TextField { promptText = "Vitamin A (mcg)" }
  private val ironField = new TextField { promptText = "Iron (mg)" }
  private val wholeGrainCheck = new CheckBox("Whole Grain")
  
  private var result: Option[Food] = None
  
  def showAndWait(): Option[Food] = {
    dialog.scene = new Scene {
      root = new VBox {
        spacing = 15
        padding = Insets(20)
        children = List(
          new Label("Add New Food to Database") {
            style = "-fx-font-size: 18px; -fx-font-weight: bold;"
          },
          
          new Separator(),
          
          new Label("Basic Information:") {
            style = "-fx-font-weight: bold;"
          },
          
          new GridPane {
            hgap = 10
            vgap = 10
            add(new Label("Name:"), 0, 0)
            add(nameField, 1, 0)
            add(new Label("Category:"), 0, 1)
            add(categoryCombo, 1, 1)
            add(new Label("Calories:"), 0, 2)
            add(caloriesField, 1, 2)
            add(new Label("Protein (g):"), 0, 3)
            add(proteinField, 1, 3)
            add(new Label("Carbs (g):"), 0, 4)
            add(carbsField, 1, 4)
            add(new Label("Fat (g):"), 0, 5)
            add(fatField, 1, 5)
          },
          
          new Separator(),
          
          new Label("Category-Specific Information:") {
            style = "-fx-font-weight: bold;"
          },
          
          categorySpecificFields,
          
          new HBox {
            spacing = 10
            children = List(
              new Button("Add Food") {
                style = "-fx-background-color: #4CAF50; -fx-text-fill: white;"
                onAction = handle { addFood() }
              },
              new Button("Cancel") {
                style = "-fx-background-color: #f44336; -fx-text-fill: white;"
                onAction = handle { dialog.close() }
              }
            )
          }
        )
      }
    }
    
    // Update category-specific fields when category changes
    categoryCombo.onAction = handle { updateCategoryFields() }
    updateCategoryFields()
    
    dialog.showAndWait()
    result
  }
  
  private def categorySpecificFields = new VBox {
    id = "categoryFields"
    spacing = 10
  }
  
  private def updateCategoryFields(): Unit = {
    // Clear and update the category-specific fields
    categorySpecificFields.children.clear()
    
    categoryCombo.value.value match {
      case "Fruit" =>
        categorySpecificFields.children ++= List(
          new Label("Vitamin C (mg):"),
          vitaminCField,
          new Label("Fiber (g):"),
          fiberField
        )
      case "Vegetable" =>
        categorySpecificFields.children ++= List(
          new Label("Vitamin A (mcg):"),
          vitaminAField,
          new Label("Iron (mg):"),
          ironField
        )
      case "Grain" =>
        categorySpecificFields.children ++= List(
          new Label("Fiber (g):"),
          fiberField,
          wholeGrainCheck
        )
    }
  }
  
  private def addFood(): Unit = {
    try {
      // Validate required fields
      if (nameField.text.value.trim.isEmpty) {
        showError("Name is required")
        return
      }
      
      val name = nameField.text.value.trim
      val calories = parseDouble(caloriesField.text.value, "Calories")
      val protein = parseDouble(proteinField.text.value, "Protein")
      val carbs = parseDouble(carbsField.text.value, "Carbohydrates")
      val fat = parseDouble(fatField.text.value, "Fat")
      
      // Validate ranges
      if (calories < 0 || calories > 10000) {
        showError("Calories must be between 0 and 10000")
        return
      }
      
      if (protein < 0 || protein > 1000) {
        showError("Protein must be between 0 and 1000g")
        return
      }
      
      if (carbs < 0 || carbs > 1000) {
        showError("Carbohydrates must be between 0 and 1000g")
        return
      }
      
      if (fat < 0 || fat > 1000) {
        showError("Fat must be between 0 and 1000g")
        return
      }
      
      val food = categoryCombo.value.value match {
        case "Fruit" =>
          val vitaminC = parseDouble(vitaminCField.text.value, "Vitamin C", 0)
          val fiber = parseDouble(fiberField.text.value, "Fiber", 0)
          Fruit(name, calories, protein, carbs, fat, vitaminC, fiber)
          
        case "Vegetable" =>
          val vitaminA = parseDouble(vitaminAField.text.value, "Vitamin A", 0)
          val iron = parseDouble(ironField.text.value, "Iron", 0)
          Vegetable(name, calories, protein, carbs, fat, vitaminA, iron)
          
        case "Grain" =>
          val fiber = parseDouble(fiberField.text.value, "Fiber", 0)
          val isWhole = wholeGrainCheck.selected.value
          Grain(name, calories, protein, carbs, fat, fiber, isWhole)
          
        case _ => throw new IllegalArgumentException("Invalid category")
      }
      
      database.addFood(food)
      result = Some(food)
      
      showSuccess(s"Successfully added ${food.name} to the database!")
      dialog.close()
      
    } catch {
      case e: NumberFormatException =>
        showError("Please enter valid numbers for all numeric fields")
      case e: IllegalArgumentException =>
        showError(e.getMessage)
      case e: Exception =>
        showError(s"Error adding food: ${e.getMessage}")
    }
  }
  
  private def parseDouble(text: String, fieldName: String, default: Double = Double.NaN): Double = {
    if (text.trim.isEmpty && !default.isNaN) default
    else if (text.trim.isEmpty) throw new IllegalArgumentException(s"$fieldName is required")
    else {
      try {
        text.trim.toDouble
      } catch {
        case _: NumberFormatException =>
          throw new NumberFormatException(s"$fieldName must be a valid number")
      }
    }
  }
  
  private def showError(message: String): Unit = {
    val alert = new Alert(Alert.AlertType.Error) {
      title = "Input Error"
      headerText = "Invalid Input"
      contentText = message
    }
    alert.showAndWait()
  }
  
  private def showSuccess(message: String): Unit = {
    val alert = new Alert(Alert.AlertType.Information) {
      title = "Success"
      headerText = "Food Added"
      contentText = message
    }
    alert.showAndWait()
  }
}