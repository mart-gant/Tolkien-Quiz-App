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
            QuestionEntity(text = "How many Rings of Power were forged for the Elven-kings?", options = "One|Three|Seven|Nine", correctAnswerIndex = 1, category = "Lore", loreNote = "Three Rings for the Elven-kings under the sky: Narya, Nenya, and Vilya."),
            QuestionEntity(text = "What is the translation of 'Mellon' from Sindarin?", options = "Friend|King|Gate|Star", correctAnswerIndex = 0, category = "Language", loreNote = "The West-gate of Moria opened when Gandalf spoke 'Mellon', the Elvish word for Friend."),
            QuestionEntity(text = "Who was the first king of the Reunited Kingdom of Gondor and Arnor?", options = "Isildur|Elendil|Elessar|Denethor", correctAnswerIndex = 2, category = "History", loreNote = "Aragorn was crowned as King Elessar Telcontar at the end of the War of the Ring."),
            QuestionEntity(text = "What was the name of the bridge in Khazad-dûm where Gandalf fought the Balrog?", options = "Durin's Bridge|The Bridge of Khazad-dûm|Stonebridge|Moria Pass", correctAnswerIndex = 1, category = "Geography", loreNote = "The Bridge of Khazad-dûm was a narrow stone arch spanning a deep chasm in Moria."),
            QuestionEntity(text = "Which Hobbit began writing the 'Red Book of Westmarch'?", options = "Frodo Baggins|Bilbo Baggins|Samwise Gamgee|Peregrin Took", correctAnswerIndex = 1, category = "Characters", loreNote = "Bilbo started the Red Book with his memoir 'There and Back Again'. Frodo and Sam later completed it."),
            QuestionEntity(text = "What is the name of the tower where Saruman lived?", options = "Barad-dûr|Minas Morgul|Orthanc|Cirith Ungol", correctAnswerIndex = 2, category = "Geography", loreNote = "Orthanc was the unbreakable black stone tower located at the center of Isengard."),
            QuestionEntity(text = "Who is the wife of Tom Bombadil?", options = "Galadriel|Arwen|Goldberry|Lúthien", correctAnswerIndex = 2, category = "Characters", loreNote = "Goldberry, the 'River-woman's daughter', is the joyful companion of Tom Bombadil in the Old Forest."),
            QuestionEntity(text = "What was the original name of Gollum?", options = "Sméagol|Déagol|Bandobras|Tobold", correctAnswerIndex = 0, category = "Characters", loreNote = "Sméagol was a Stoorish Hobbit before finding the One Ring and being corrupted into Gollum."),
            QuestionEntity(text = "Which creature is a 'Shelob'?", options = "Dragon|Giant Spider|Orc|Troll", correctAnswerIndex = 1, category = "Creatures", loreNote = "Shelob was a great spider, last child of Ungoliant, lair-dwelling in Cirith Ungol."),
            QuestionEntity(text = "What was the name of the White Tree of Númenor, ancestor of Gondor's tree?", options = "Nimloth|Telperion|Laurelin|Mallorn", correctAnswerIndex = 0, category = "Lore", loreNote = "Nimloth grew in Armenelos, Númenor. Isildur saved one seedling before Númenor was destroyed."),
            QuestionEntity(text = "What was the name of the dragon killed by Bard the Bowman?", options = "Ancalagon|Smaug|Glaurung|Scatha", correctAnswerIndex = 1, category = "Creatures", loreNote = "Smaug the Golden had seized Erebor until Bard shot his single bare spot with the Black Arrow."),
            QuestionEntity(text = "Who was the creator of the Silmarils?", options = "Fëanor|Fingolfin|Finwë|Lúthien", correctAnswerIndex = 0, category = "History", loreNote = "Fëanor forged the three Silmarils containing the sacred Light of the Two Trees of Valinor."),
            QuestionEntity(text = "What is the name of the sword wielded by King Elendil?", options = "Glamdring|Andúril|Narsil|Sting", correctAnswerIndex = 2, category = "Items", loreNote = "Narsil broke when Elendil fell fighting Sauron; it was later reforged into Andúril for Aragorn."),
            QuestionEntity(text = "Which Elven realm was ruled by Galadriel and Celeborn?", options = "Rivendell|Mirkwood|Lothlórien|Grey Havens", correctAnswerIndex = 2, category = "Geography", loreNote = "Lothlórien was the Golden Wood protected by Galadriel using the Elven Ring Nenya."),
            QuestionEntity(text = "How many members were in the Fellowship of the Ring?", options = "Seven|Eight|Nine|Ten", correctAnswerIndex = 2, category = "General", loreNote = "The Nine Walkers represented the Free Peoples against the Nine Black Riders of Sauron."),
            QuestionEntity(text = "Which Great Eagle rescued Gandalf from Orthanc and Zirakzigil?", options = "Gwaihir|Landroval|Meneldor|Thorondor", correctAnswerIndex = 0, category = "Creatures", loreNote = "Gwaihir the Windlord, swiftest of the Great Eagles, rescued Gandalf on multiple occasions."),
            QuestionEntity(text = "Who was the father of Legolas?", options = "Elrond|Thranduil|Celeborn|Círdan", correctAnswerIndex = 1, category = "Characters", loreNote = "Thranduil was the King of the Woodland Realm of Elves in Mirkwood."),
            QuestionEntity(text = "What is the name of the inn where the Hobbits met Strider?", options = "The Green Dragon|The Ivy Bush|The Prancing Pony|The Golden Perch", correctAnswerIndex = 2, category = "Geography", loreNote = "The Prancing Pony in Bree was kept by Barliman Butterbur."),
            QuestionEntity(text = "Which mountain is also known as Orodruin?", options = "Lonely Mountain|Mount Doom|Caradhras|Weathertop", correctAnswerIndex = 1, category = "Geography", loreNote = "Orodruin ('Mountain of Fire') is Mount Doom in Mordor where the One Ring was forged."),
            QuestionEntity(text = "What was the name of Gandalf's sword?", options = "Glamdring|Orcrist|Sting|Narsil", correctAnswerIndex = 0, category = "Items", loreNote = "Glamdring ('Foe-hammer') was forged in Gondolin and discovered by Gandalf in a troll hoard."),
            QuestionEntity(text = "Who killed the Witch-king of Angmar?", options = "Aragorn|Éowyn|Éomer|Gandalf", correctAnswerIndex = 1, category = "History", loreNote = "Fulfilling Glorfindel's prophecy ('Not by the hand of man shall he fall'), Éowyn slew the Witch-king."),
            QuestionEntity(text = "What were the names of the two trees of Valinor?", options = "Telperion and Laurelin|Nimloth and Galathilion|Hirilorn and Brethil|Culúrien and Silpion", correctAnswerIndex = 0, category = "Lore", loreNote = "Telperion (silver) and Laurelin (gold) lit the land of Valinor before being destroyed by Ungoliant."),
            QuestionEntity(text = "What was the name of the island kingdom of Men that was destroyed at the end of the Second Age?", options = "Beleriand|Númenor|Umbar|Arnor", correctAnswerIndex = 1, category = "History", loreNote = "Númenor was drowned beneath the sea after King Ar-Pharazôn attempted to invade Valinor."),
            QuestionEntity(text = "Who found the One Ring in the River Anduin after Isildur's death?", options = "Sméagol|Bilbo|Déagol|Gollum", correctAnswerIndex = 2, category = "History", loreNote = "Déagol found the Ring while fishing on his birthday; Sméagol choked him and stole it."),
            QuestionEntity(text = "Which wizard is known as 'The Brown'?", options = "Gandalf|Saruman|Radagast|Alatar", correctAnswerIndex = 2, category = "Characters", loreNote = "Radagast the Brown was an Istari devoted to the birds, beasts, and nature of Middle-earth."),
            QuestionEntity(text = "What is the name of the secret entrance to the kingdom of Erebor?", options = "The West Gate|The Side-door|The Hidden Path|The Dragon's Breach", correctAnswerIndex = 1, category = "Geography", loreNote = "The Side-door of Erebor could only be opened on Durin's Day when the thrush knocked."),
            QuestionEntity(text = "Who is the father of Elrond?", options = "Eärendil|Gil-galad|Círdan|Celeborn", correctAnswerIndex = 0, category = "Characters", loreNote = "Eärendil the Mariner sailed to Valinor to ask the Valar for aid against Morgoth."),
            QuestionEntity(text = "What was the name of the fortress of Sauron in Mirkwood?", options = "Barad-dûr|Dol Guldur|Isengard|Angmar", correctAnswerIndex = 1, category = "Geography", loreNote = "Dol Guldur ('Hill of Sorcery') was Sauron's stronghold in southern Mirkwood as the Necromancer."),
            QuestionEntity(text = "What was the name of the great wolf that bit off Beren's hand?", options = "Draugluin|Carcharoth|Huan|Wulf", correctAnswerIndex = 1, category = "Creatures", loreNote = "Carcharoth devoured Beren's hand together with the Silmaril, driving the wolf mad with pain."),
            QuestionEntity(text = "Who was the first king of Rohan?", options = "Helm Hammerhand|Theoden|Eorl the Young|Fengel", correctAnswerIndex = 2, category = "History", loreNote = "Eorl the Young led the Éothéod to aid Gondor and was granted the land of Calenardhon (Rohan)."),
            QuestionEntity(text = "What was the name of the sword belonging to Thorin Oakenshield?", options = "Orcrist|Glamdring|Sting|Foe-hammer", correctAnswerIndex = 0, category = "Items", loreNote = "Orcrist ('Goblin-cleaver') was found alongside Glamdring in the troll hoard."),
            QuestionEntity(text = "Which Hobbit was the tallest in history after drinking Ent-draught?", options = "Frodo|Sam|Merry and Pippin|Bilbo", correctAnswerIndex = 2, category = "Characters", loreNote = "Merry and Pippin drank Ent-draughts in Fangorn, growing over four feet tall and surpassing Bandobras Took."),
            QuestionEntity(text = "What is the name of the pass where Shelob lived?", options = "Cirith Ungol|The Redhorn Pass|The Gap of Rohan|Caradhras", correctAnswerIndex = 0, category = "Geography", loreNote = "Cirith Ungol ('Pass of the Spider') was the steep mountain pass leading into Mordor."),
            QuestionEntity(text = "Who was the High King of the Noldor in Beleriand who challenged Morgoth to single combat?", options = "Fëanor|Fingolfin|Finrod|Turgon", correctAnswerIndex = 1, category = "History", loreNote = "Fingolfin rode to Angband alone in wrath and gave Morgoth seven wounds before being slain."),
            QuestionEntity(text = "What was the name of the hidden city built by Turgon?", options = "Nargothrond|Gondolin|Menegroth|Doriath", correctAnswerIndex = 1, category = "Geography", loreNote = "Gondolin ('The Hidden City') stood in the plain of Tumladen, encircled by the Encircling Mountains."),
            QuestionEntity(text = "Who killed the dragon Glaurung?", options = "Beren|Túrin Turambar|Tuor|Eärendil", correctAnswerIndex = 1, category = "History", loreNote = "Túrin Turambar slew Glaurung Father of Dragons at the ravine of Cabed-en-Araras with Gurthang."),
            QuestionEntity(text = "What was the name of the ship Eärendil sailed to Valinor?", options = "Vingilot|Eärrámë|Ciris|Entulessë", correctAnswerIndex = 0, category = "Items", loreNote = "Vingilot ('Foam-flower') was hallowed by the Valar and set into the sky carrying the Silmaril."),
            QuestionEntity(text = "Who is the master of the sea in Tolkien's mythology?", options = "Manwë|Ulmo|Aulë|Oromë", correctAnswerIndex = 1, category = "Lore", loreNote = "Ulmo, Lord of Waters, never abandoned the Children of Ilúvatar in Beleriand."),
            QuestionEntity(text = "What was the name of the two blue wizards?", options = "Alatar and Pallando|Radagast and Gandalf|Saruman and Alatar|Morgoth and Sauron", correctAnswerIndex = 0, category = "Characters", loreNote = "Alatar and Pallando (the Ithryn Luin) traveled into the far East of Middle-earth."),
            QuestionEntity(text = "What is the name of the Elvish bread that can sustain a traveler for a day?", options = "Miruvor|Lembas|Cram|Galenas", correctAnswerIndex = 1, category = "Items", loreNote = "Lembas waybread was wrapped in mallorn leaves and kept travelers strong on long journeys."),
            QuestionEntity(text = "Who was the mother of Arwen?", options = "Galadriel|Celebrían|Lúthien|Idril", correctAnswerIndex = 1, category = "Characters", loreNote = "Celebrían was the daughter of Galadriel and Celeborn, and wife of Elrond."),
            QuestionEntity(text = "What was the name of the great dome in Osgiliath that housed its Palantír?", options = "Dome of the Stars|Dome of Osgiliath|Tower of the Sun|Dome of Ithildin", correctAnswerIndex = 0, category = "Geography", loreNote = "The Dome of the Stars housed the chief Palantír of the South until the city fell in TA 1432."),
            QuestionEntity(text = "Which Valar is the master of the Halls of Mandos?", options = "Námo|Irmo|Tulkas|Nienna", correctAnswerIndex = 0, category = "Lore", loreNote = "Námo (commonly called Mandos) is the keeper of the Houses of the Dead in Valinor."),
            QuestionEntity(text = "What was the name of the faithful hound that helped Beren and Lúthien?", options = "Huan|Carcharoth|Draugluin|Garm", correctAnswerIndex = 0, category = "Creatures", loreNote = "Huan, the Hound of Valinor, was granted speech three times before fulfilling his destiny."),
            QuestionEntity(text = "How many Palantíri were brought to Middle-earth?", options = "Three|Seven|Nine|One", correctAnswerIndex = 1, category = "Items", loreNote = "Seven Seeing-stones were brought by Elendil and his sons from Númenor."),
            QuestionEntity(text = "What is the name of the mountain range that protects Valinor?", options = "Hithaeglir|Pelóri|Ered Luin|Thangorodrim", correctAnswerIndex = 1, category = "Geography", loreNote = "The Pelóri mountains were raised by the Valar to guard Valinor from Morgoth."),
            QuestionEntity(text = "Who was the father of Faramir and Boromir?", options = "Theoden|Denethor II|Aragorn|Isildur", correctAnswerIndex = 1, category = "Characters", loreNote = "Denethor II was the 26th and last Ruling Steward of Gondor."),
            QuestionEntity(text = "What was the name of the ancient city of the Elves in Beleriand, ruled by Thingol?", options = "Menegroth|Gondolin|Nargothrond|Vinyamar", correctAnswerIndex = 0, category = "Geography", loreNote = "Menegroth ('The Thousand Caves') was the hidden capital of Doriath."),
            QuestionEntity(text = "What is the name of the period before the First Age?", options = "The Years of the Trees|The Second Age|The Third Age|The Elder Days", correctAnswerIndex = 0, category = "History", loreNote = "The Years of the Trees lasted from the creation of Valinor until the first rising of the Sun and Moon."),
            QuestionEntity(text = "Who eventually possessed the Ring of Adamant (Nenya)?", options = "Elrond|Círdan|Galadriel|Gandalf", correctAnswerIndex = 2, category = "Items", loreNote = "Nenya, the Ring of Water, was borne by Galadriel to preserve the beauty of Lothlórien.")
        )
    }
}
