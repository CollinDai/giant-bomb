package peike.rkt.home


data class SearchResponses(
  val error: String,
  val limit: Int,
  val offset: Int,
  val number_of_page_results: Int,
  val number_of_total_results: Int,
  val status_code: Int,
  val results: List<Game>
)

data class Game(
  val aliases: Any?,
  val api_detail_url: String,
  val date_added: String,
  val date_last_updated: String,
  val deck: String,
  val description: String,
  val expected_release_day: Any?,
  val expected_release_month: Any?,
  val expected_release_quarter: Any?,
  val expected_release_year: Any?,
  val guid: String,
  val id: Int,
  val image: Image,
  val image_tags: List<ImageTag>,
  val name: String,
  val number_of_user_reviews: Int,
  val original_game_rating: List<OriginalGameRating>,
  val original_release_date: String,
  val platforms: List<Platform>,
  val site_detail_url: String
)

data class Image(
  val icon_url: String,
  val medium_url: String,
  val screen_url: String,
  val screen_large_url: String,
  val small_url: String,
  val super_url: String,
  val thumb_url: String,
  val tiny_url: String,
  val original_url: String,
  val image_tags: String
)

data class ImageTag(
  val api_detail_url: String,
  val name: String,
  val total: Int
)

data class OriginalGameRating(
  val api_detail_url: String,
  val id: Int,
  val name: String
)

data class Platform(
  val api_detail_url: String,
  val id: Int,
  val name: String,
  val site_detail_url: String,
  val abbreviation: String
)
