package nutrition

abstract class Food(
  val name: String,
  val calories: Double,
  val protein: Double,
  val carbohydrates: Double,
  val fat: Double
) {
  def getCategory: String
  def getNutritionSummary: String = {
    f"$name: ${calories}%.1f cal, ${protein}%.1f g protein, ${carbohydrates}%.1f g carbs, ${fat}%.1f g fat"
  }
  def isHealthy: Boolean = calories < 500 && fat < 20
  
  override def toString: String = s"$name (${getCategory})"
}