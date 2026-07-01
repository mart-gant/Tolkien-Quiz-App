package com.example.data.repository

import android.util.Log
import com.example.data.local.dao.QuestionDao
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.preferences.UserPreferences
import com.example.data.mapper.toDomain
import com.example.data.remote.QuizApi
import com.example.data.remote.dto.toEntity
import com.example.domain.model.Question
import com.example.domain.repository.QuizRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val userPreferences: UserPreferences,
    private val quizApi: QuizApi,
) : QuizRepository {

    override fun getQuestions(): Flow<List<Question>> = flow {
        val localData = questionDao.getQuestions().first().map { it.toDomain() }
        if (localData.isNotEmpty()) {
            emit(localData)
        }

        try {
            val remoteQuestions = quizApi.getQuestions()
            if (remoteQuestions.isNotEmpty()) {
                questionDao.insertQuestions(remoteQuestions.map { it.toEntity() })
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Network fetch failed: ${e.message}")
            if (questionDao.getQuestionCount() < 50) {
                questionDao.insertQuestions(getInitialQuestions())
            }
        }

        emitAll(
            questionDao.getQuestions().map { entities ->
                entities.map { it.toDomain() }
            }
        )
    }.distinctUntilChanged()

    override fun getHighScore(): Flow<Int> = userPreferences.highScore

    override suspend fun saveHighScore(score: Int) {
        userPreferences.saveHighScore(score)
    }

    private fun getInitialQuestions(): List<QuestionEntity> {
        return listOf(
            QuestionEntity(text = "How many Rings of Power were forged for the Elven-kings?", options = "One|Three|Seven|Nine", correctAnswerIndex = 1, category = "Lore"),
            QuestionEntity(text = "What is the translation of 'Mellon' from Sindarin?", options = "Friend|King|Gate|Star", correctAnswerIndex = 0, category = "Language"),
            QuestionEntity(text = "Who was the first king of the Reunited Kingdom of Gondor and Arnor?", options = "Isildur|Elendil|Elessar|Denethor", correctAnswerIndex = 2, category = "History"),
            QuestionEntity(text = "What was the name of the bridge in Khazad-dûm where Gandalf fought the Balrog?", options = "Durin's Bridge|The Bridge of Khazad-dûm|Stonebridge|Moria Pass", correctAnswerIndex = 1, category = "Geography"),
            QuestionEntity(text = "Which Hobbit was known for his 'red book'?", options = "Frodo Baggins|Bilbo Baggins|Samwise Gamgee|Peregrin Took", correctAnswerIndex = 1, category = "Characters"),
            QuestionEntity(text = "What is the name of the tower where Saruman lived?", options = "Barad-dûr|Minas Morgul|Orthanc|Cirith Ungol", correctAnswerIndex = 2, category = "Geography"),
            QuestionEntity(text = "Who is the wife of Tom Bombadil?", options = "Galadriel|Arwen|Goldberry|Lúthien", correctAnswerIndex = 2, category = "Characters"),
            QuestionEntity(text = "What was the original name of Gollum?", options = "Sméagol|Déagol|Bandobras|Tobold", correctAnswerIndex = 0, category = "Characters"),
            QuestionEntity(text = "Which creature is a 'Shelob'?", options = "Dragon|Giant Spider|Orc|Troll", correctAnswerIndex = 1, category = "Creatures"),
            QuestionEntity(text = "What is the name of the white tree of Gondor?", options = "Nimloth|Telperion|Laurelin|Mallorn", correctAnswerIndex = 0, category = "Lore"),
            QuestionEntity(text = "What was the name of the dragon killed by Bard the Bowman?", options = "Ancalagon|Smaug|Glaurung|Scatha", correctAnswerIndex = 1, category = "Creatures"),
            QuestionEntity(text = "Who was the creator of the Silmarils?", options = "Fëanor|Fingolfin|Finwë|Lúthien", correctAnswerIndex = 0, category = "History"),
            QuestionEntity(text = "What is the name of the sword wielded by King Elendil?", options = "Glamdring|Andúril|Narsil|Sting", correctAnswerIndex = 2, category = "Items"),
            QuestionEntity(text = "Which Elven realm was ruled by Galadriel and Celeborn?", options = "Rivendell|Mirkwood|Lothlórien|Grey Havens", correctAnswerIndex = 2, category = "Geography"),
            QuestionEntity(text = "How many members were in the Fellowship of the Ring?", options = "Seven|Eight|Nine|Ten", correctAnswerIndex = 2, category = "General"),
            QuestionEntity(text = "What is the name of the giant eagle that rescued Frodo and Sam?", options = "Gwaihir|Landroval|Meneldor|Thorondor", correctAnswerIndex = 0, category = "Creatures"),
            QuestionEntity(text = "Who was the father of Legolas?", options = "Elrond|Thranduil|Celeborn|Círdan", correctAnswerIndex = 1, category = "Characters"),
            QuestionEntity(text = "What is the name of the inn where the Hobbits met Strider?", options = "The Green Dragon|The Ivy Bush|The Prancing Pony|The Golden Perch", correctAnswerIndex = 2, category = "Geography"),
            QuestionEntity(text = "Which mountain is also known as Orodruin?", options = "Lonely Mountain|Mount Doom|Caradhras|Weathertop", correctAnswerIndex = 1, category = "Geography"),
            QuestionEntity(text = "What was the name of Gandalf's sword?", options = "Glamdring|Orcrist|Sting|Narsil", correctAnswerIndex = 0, category = "Items"),
            QuestionEntity(text = "Who killed the Witch-king of Angmar?", options = "Aragorn|Éowyn|Éomer|Gandalf", correctAnswerIndex = 1, category = "History"),
            QuestionEntity(text = "What were the names of the two trees of Valinor?", options = "Telperion and Laurelin|Nimloth and Galathilion|Hirilorn and Brethil|Culúrien and Silpion", correctAnswerIndex = 0, category = "Lore"),
            QuestionEntity(text = "What was the name of the island kingdom of Men that was destroyed at the end of the Second Age?", options = "Beleriand|Númenor|Umbar|Arnor", correctAnswerIndex = 1, category = "History"),
            QuestionEntity(text = "Who found the One Ring in the River Anduin after Isildur's death?", options = "Sméagol|Bilbo|Déagol|Gollum", correctAnswerIndex = 2, category = "History"),
            QuestionEntity(text = "Which wizard is known as 'The Brown'?", options = "Gandalf|Saruman|Radagast|Alatar", correctAnswerIndex = 2, category = "Characters"),
            QuestionEntity(text = "What is the name of the secret entrance to the kingdom of Erebor?", options = "The West Gate|The Side-door|The Hidden Path|The Dragon's Breach", correctAnswerIndex = 1, category = "Geography"),
            QuestionEntity(text = "Who is the father of Elrond?", options = "Eärendil|Gil-galad|Círdan|Celeborn", correctAnswerIndex = 0, category = "Characters"),
            QuestionEntity(text = "What was the name of the fortress of Sauron in Mirkwood?", options = "Barad-dûr|Dol Guldur|Isengard|Angmar", correctAnswerIndex = 1, category = "Geography"),
            QuestionEntity(text = "What was the name of the great wolf that bit off Beren's hand?", options = "Draugluin|Carcharoth|Huan|Wulf", correctAnswerIndex = 1, category = "Creatures"),
            QuestionEntity(text = "Who was the first king of Rohan?", options = "Helm Hammerhand|Theoden|Eorl the Young|Fengel", correctAnswerIndex = 2, category = "History"),
            QuestionEntity(text = "What was the name of the sword belonging to Thorin Oakenshield?", options = "Orcrist|Glamdring|Sting|Foe-hammer", correctAnswerIndex = 0, category = "Items"),
            QuestionEntity(text = "Which Hobbit was the tallest in history after drinking Ent-draught?", options = "Frodo|Sam|Merry and Pippin|Bilbo", correctAnswerIndex = 2, category = "Characters"),
            QuestionEntity(text = "What is the name of the pass where Shelob lived?", options = "Cirith Ungol|The Redhorn Pass|The Gap of Rohan|Caradhras", correctAnswerIndex = 0, category = "Geography"),
            QuestionEntity(text = "Who was the High King of the Noldor in Beleriand who challenged Morgoth to single combat?", options = "Fëanor|Fingolfin|Finrod|Turgon", correctAnswerIndex = 1, category = "History"),
            QuestionEntity(text = "What was the name of the hidden city built by Turgon?", options = "Nargothrond|Gondolin|Menegroth|Doriath", correctAnswerIndex = 1, category = "Geography"),
            QuestionEntity(text = "Who killed the dragon Glaurung?", options = "Beren|Túrin Turambar|Tuor|Eärendil", correctAnswerIndex = 1, category = "History"),
            QuestionEntity(text = "What was the name of the ship Eärendil sailed to Valinor?", options = "Vingilot|Eärrámë|Ciris|Entulessë", correctAnswerIndex = 0, category = "Items"),
            QuestionEntity(text = "Who is the master of the sea in Tolkien's mythology?", options = "Manwë|Ulmo|Aulë|Oromë", correctAnswerIndex = 1, category = "Lore"),
            QuestionEntity(text = "What was the name of the two blue wizards?", options = "Alatar and Pallando|Radagast and Gandalf|Saruman and Alatar|Morgoth and Sauron", correctAnswerIndex = 0, category = "Characters"),
            QuestionEntity(text = "What is the name of the Elvish bread that can sustain a traveler for a day?", options = "Miruvor|Lembas|Cram|Galenas", correctAnswerIndex = 1, category = "Items"),
            QuestionEntity(text = "Who was the mother of Arwen?", options = "Galadriel|Celebrían|Lúthien|Idril", correctAnswerIndex = 1, category = "Characters"),
            QuestionEntity(text = "What was the name of the bridge that leads to the gates of Osgiliath?", options = "Osgiliath Bridge|The Harlond|Pelargir|Ethir", correctAnswerIndex = 0, category = "Geography"),
            QuestionEntity(text = "Which Valar is the master of the Halls of Mandos?", options = "Námo|Irmo|Tulkas|Nienna", correctAnswerIndex = 0, category = "Lore"),
            QuestionEntity(text = "What was the name of the faithful hound that helped Beren and Lúthien?", options = "Huan|Carcharoth|Draugluin|Garm", correctAnswerIndex = 0, category = "Creatures"),
            QuestionEntity(text = "How many Palantíri were brought to Middle-earth?", options = "Three|Seven|Nine|One", correctAnswerIndex = 1, category = "Items"),
            QuestionEntity(text = "What is the name of the mountain range that protects Valinor?", options = "Hithaeglir|Pelóri|Ered Luin|Thangorodrim", correctAnswerIndex = 1, category = "Geography"),
            QuestionEntity(text = "Who was the father of Faramir and Boromir?", options = "Theoden|Denethor II|Aragorn|Isildur", correctAnswerIndex = 1, category = "Characters"),
            QuestionEntity(text = "What was the name of the ancient city of the Elves in Beleriand, ruled by Thingol?", options = "Menegroth|Gondolin|Nargothrond|Vinyamar", correctAnswerIndex = 0, category = "Geography"),
            QuestionEntity(text = "What is the name of the period before the First Age?", options = "The Years of the Trees|The Second Age|The Third Age|The Elder Days", correctAnswerIndex = 0, category = "History"),
            QuestionEntity(text = "Who eventually possessed the Ring of Adamant (Nenya)?", options = "Elrond|Círdan|Galadriel|Gandalf", correctAnswerIndex = 2, category = "Items")
        )
    }
}
