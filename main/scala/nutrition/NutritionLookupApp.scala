package nutrition

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.geometry.{Insets, Pos}
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.Includes._
import scala.jdk.CollectionConverters._

object NutritionLookupApp extends JFXApp3 {

  // Holds displayed values for each food table row
  class FoodDisplay(food: Food) {
    val name = StringProperty(food.name)
    val category = StringProperty(food.getCategory)
    val calories = StringProperty(f"${food.calories}%.1f")
    val protein = StringProperty(f"${food.protein}%.1f g")
    val carbs = StringProperty(f"${food.carbohydrates}%.1f g")
    val fat = StringProperty(f"${food.fat}%.1f g")
    val summary = StringProperty(food.getNutritionSummary)
    val healthy = StringProperty(if (food.isHealthy) "Yes" else "No")

    def getFood: Food = food
  }

  override def start(): Unit = {
    val database = new NutritionDatabase()

    stage = new PrimaryStage {
      title = "Nutrition Information Lookup System - UN SDG 2: Zero Hunger"
      width = 1000
      height = 700

      scene = new Scene {
        fill = scalafx.scene.paint.Color.White

        root = new BorderPane {

          // Creates the application’s main top menu bar
          top = new MenuBar {
            menus = List(
              new Menu("File") {
                items = List(
                  new MenuItem("Add New Food") {
                    onAction = (_: ActionEvent) => {
                      val addDialog = new AddFoodDialog(stage, database)
                      val newFood = addDialog.showAndWait()
                      if (newFood.isDefined) {
                        updateResults(database.getAllFoods)
                        updateStatus()
                      }
                    }
                  },
                  new SeparatorMenuItem(),
                  new MenuItem("Exit") {
                    onAction = (_: ActionEvent) => System.exit(0)
                  }
                )
              },
              new Menu("View") {
                items = List(
                  new MenuItem("Show All Foods") {
                    onAction = (_: ActionEvent) => updateResults(database.getAllFoods)
                  },
                  new MenuItem("Show Database Summary") {
                    onAction = (_: ActionEvent) => {
                      new Alert(Alert.AlertType.Information) {
                        title = "Database Summary"
                        headerText = "Nutrition Database Statistics"
                        contentText = database.getNutritionSummary
                      }.showAndWait()
                    }
                  }
                )
              },
              new Menu("Help") {
                items = List(
                  new MenuItem("About") {
                    onAction = (_: ActionEvent) => {
                      new Alert(Alert.AlertType.Information) {
                        title = "About"
                        headerText = "Nutrition Information Lookup System"
                        contentText = "Supporting UN Sustainable Development Goal 2: Zero Hunger\n\nThis application helps users access nutritional information to make informed dietary choices."
                      }.showAndWait()
                    }
                  }
                )
              }
            )
          }

          // Builds the main search and filter section
          center = new VBox {
            spacing = 10
            padding = Insets(20)

            children = List(
              new Label("Nutrition Information Lookup System") {
                style = "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;"
              },

              new Label("Supporting UN SDG 2: Zero Hunger - Access nutritional information for better health") {
                style = "-fx-font-size: 14px; -fx-text-fill: #666666;"
              },

              new Separator(),

              // Search input and action buttons row
              new HBox {
                spacing = 10
                alignment = Pos.CenterLeft
                children = List(
                  new Label("Search by Name:") {
                    minWidth = 120
                  },
                  new TextField {
                    id = "searchField"
                    promptText = "Enter food name (e.g., apple, broccoli, rice)"
                    prefWidth = 300
                  },
                  new Button("Search") {
                    id = "searchButton"
                    style = "-fx-background-color: #4CAF50; -fx-text-fill: white;"
                  },
                  new Button("Clear") {
                    id = "clearButton"
                    style = "-fx-background-color: #FF9800; -fx-text-fill: white;"
                  }
                )
              },

              // Filter foods by category or healthiness
              new HBox {
                spacing = 10
                alignment = Pos.CenterLeft
                children = List(
                  new Label("Filter by Category:") {
                    minWidth = 120
                  },
                  new ComboBox[String] {
                    id = "categoryCombo"
                    items = ObservableBuffer("All") ++ ObservableBuffer(database.getCategories: _*)
                    value = "All"
                    prefWidth = 200
                  },
                  new Button("Show Healthy Foods Only") {
                    id = "healthyButton"
                    style = "-fx-background-color: #8BC34A; -fx-text-fill: white;"
                  }
                )
              },

              new Separator(),

              // Displays the search results in a table
              new Label("Search Results:") {
                style = "-fx-font-size: 16px; -fx-font-weight: bold;"
              },

              new TableView[FoodDisplay] {
                id = "resultsTable"
                columns ++= List(
                  new TableColumn[FoodDisplay, String] {
                    text = "Name"
                    cellValueFactory = { cellData => cellData.value.name }
                    prefWidth = 120
                  },
                  new TableColumn[FoodDisplay, String] {
                    text = "Category"
                    cellValueFactory = { cellData => cellData.value.category }
                    prefWidth = 100
                  },
                  new TableColumn[FoodDisplay, String] {
                    text = "Calories"
                    cellValueFactory = { cellData => cellData.value.calories }
                    prefWidth = 80
                  },
                  new TableColumn[FoodDisplay, String] {
                    text = "Protein"
                    cellValueFactory = { cellData => cellData.value.protein }
                    prefWidth = 80
                  },
                  new TableColumn[FoodDisplay, String] {
                    text = "Carbs"
                    cellValueFactory = { cellData => cellData.value.carbs }
                    prefWidth = 80
                  },
                  new TableColumn[FoodDisplay, String] {
                    text = "Fat"
                    cellValueFactory = { cellData => cellData.value.fat }
                    prefWidth = 80
                  },
                  new TableColumn[FoodDisplay, String] {
                    text = "Healthy"
                    cellValueFactory = { cellData => cellData.value.healthy }
                    prefWidth = 70
                  }
                )

                // Populates table with all foods initially
                items = ObservableBuffer(database.getAllFoods.map(new FoodDisplay(_)): _*)

                // Shows details when row is double-clicked
                onMouseClicked = _ => {
                  val selectedItem = selectionModel().getSelectedItem
                  if (selectedItem != null) {
                    showDetailedInfo(selectedItem.getFood)
                  }
                }
              }
            )
          }

          // Displays total foods and average calories
          bottom = new HBox {
            padding = Insets(10)
            spacing = 20
            alignment = Pos.CenterLeft
            style = "-fx-background-color: #E8F5E8;"
            children = List(
              new Label {
                id = "statusLabel"
                text = s"Total Foods: ${database.getFoodCount} | Average Calories: ${database.getAverageCalories.formatted("%.1f")}"
              }
            )
          }
        }

        def getSearchField = root.value.lookup("#searchField").asInstanceOf[javafx.scene.control.TextField]
        def getCategoryCombo = root.value.lookup("#categoryCombo").asInstanceOf[javafx.scene.control.ComboBox[String]]
        def getResultsTable = root.value.lookup("#resultsTable").asInstanceOf[javafx.scene.control.TableView[FoodDisplay]]
        def getStatusLabel = root.value.lookup("#statusLabel").asInstanceOf[javafx.scene.control.Label]

        // Updates the table contents with given food list
        def updateResults(foods: List[Food]): Unit = {
          val table = getResultsTable
          table.setItems(
            javafx.collections.FXCollections.observableArrayList(
              foods.map(new FoodDisplay(_)).asJava
            )
          )
          updateStatus(foods.size)
        }

        // Updates status bar with current statistics
        def updateStatus(showing: Int = -1): Unit = {
          val showingCount = if (showing >= 0) showing else database.getFoodCount
          getStatusLabel.setText(s"Showing: $showingCount foods | Total: ${database.getFoodCount} | Avg Calories: ${database.getAverageCalories.formatted("%.1f")}")
        }

        // Shows a detailed popup for selected food
        def showDetailedInfo(food: Food): Unit = {
          new Alert(Alert.AlertType.Information) {
            title = "Detailed Nutrition Information"
            headerText = s"${food.name} (${food.getCategory})"
            contentText = food.getNutritionSummary + "\n\n" +
              (food match {
                case f: NutrientRich => s"Key Nutrients: ${f.getKeyNutrients.mkString(", ")}"
                case _ => ""
              }) +
              s"\nHealth Rating: ${if (food.isHealthy) "Healthy Choice ✓" else "Moderate"}"
          }.showAndWait()
        }

        // Handles search button click and performs search
        root.value.lookup("#searchButton").onMouseClicked = _ => {
          val searchText = getSearchField.getText

          if (searchText.length > 100) {
            new Alert(Alert.AlertType.Warning) {
              title = "Input Too Long"
              headerText = "Search text is too long"
              contentText = "Please enter a search term with fewer than 100 characters."
            }.showAndWait()
          } else {
            val cleanSearchText = searchText.replaceAll("[<>\"'&]", "").trim
            getSearchField.setText(cleanSearchText)

            val results = if (cleanSearchText.isEmpty) database.getAllFoods
            else database.searchByName(cleanSearchText)

            if (results.isEmpty && cleanSearchText.nonEmpty) {
              new Alert(Alert.AlertType.Information) {
                title = "No Results"
                headerText = "Search returned no results"
                contentText = s"No foods found matching '$cleanSearchText'. Try a different search term or browse all foods."
              }.showAndWait()
            }

            updateResults(results)
          }
        }

        // Clears search input and shows all foods
        root.value.lookup("#clearButton").onMouseClicked = _ => {
          getSearchField.setText("")
          getCategoryCombo.setValue("All")
          updateResults(database.getAllFoods)
        }

        // Filters the table based on selected category
        getCategoryCombo.onAction = _ => {
          val category = getCategoryCombo.getValue
          val results = database.searchByCategory(category)
          updateResults(results)
        }

        // Filters the table to show only healthy foods
        root.value.lookup("#healthyButton").onMouseClicked = _ => {
          val results = database.getHealthyFoods
          updateResults(results)
        }

        // Executes search when Enter key is pressed
        getSearchField.onKeyPressed = handle { (event: javafx.scene.input.KeyEvent) =>
          if (event.getCode.toString == "ENTER") {
            root.value.lookup("#searchButton").fireEvent(new javafx.scene.input.MouseEvent(
              javafx.scene.input.MouseEvent.MOUSE_CLICKED,
              0, 0, 0, 0,
              javafx.scene.input.MouseButton.PRIMARY,
              1,
              false, false, false, false,
              false, false, false, false, false, false,
              null
            ))
          }
        }
      }
    }
  }

  // Converts Scala Seq to Java List for table population
  import java.util.{List => JList}
  implicit def seqToJavaList[T](seq: Seq[T]): JList[T] = {
    val javaList = new java.util.ArrayList[T]()
    seq.foreach(javaList.add)
    javaList
  }
}
