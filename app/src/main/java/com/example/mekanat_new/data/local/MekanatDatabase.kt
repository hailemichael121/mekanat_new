package com.example.mekanat_new.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ChurchEntity::class,
        TabotEntity::class,
        GubaeEventEntity::class,
        FavoriteEntity::class,
        SavedNigsEntity::class,
        ChurchPhotoEntity::class,
        SearchHistoryEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MekanatDatabase : RoomDatabase() {
    abstract fun churchDao(): ChurchDao
    abstract fun tabotDao(): TabotDao
    abstract fun gubaeDao(): GubaeDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun savedNigsDao(): SavedNigsDao
    abstract fun churchPhotoDao(): ChurchPhotoDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: MekanatDatabase? = null

        fun getDatabase(context: Context): MekanatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MekanatDatabase::class.java,
                    "mekanat_orthodox.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed database asynchronously
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { database ->
                                seedDatabase(database)
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedDatabase(db: MekanatDatabase) {
            val churchDao = db.churchDao()
            val tabotDao = db.tabotDao()
            val gubaeDao = db.gubaeDao()
            val favoriteDao = db.favoriteDao()

            val churches = listOf(
                ChurchEntity(
                    id = 1,
                    name = "Biete Giyorgis (Church of St. George)",
                    nameAmharic = "ቤተ ጊዮርጊስ (ላሊበላ)",
                    latitude = 12.0319,
                    longitude = 39.0411,
                    region = "Lalibela, Amhara",
                    diocese = "North Wollo Diocese",
                    churchType = "ROCK_HEWN",
                    description = "Iconic monolithic rock-hewn church carved downward from volcanic tuff in the shape of a symmetrical Greek cross.",
                    history = "Carved in the late 12th century under King Gebre Mesqel Lalibela. EOTC tradition tells that St. George himself rode on horseback to oversee its completion.",
                    address = "Lalibela Sacred Complex, Amhara",
                    contactPhone = "+251 33 336 0021",
                    contactEmail = "lalibela.diocese@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 2,
                    name = "Church of Our Lady Mary of Zion",
                    nameAmharic = "ርዕሰ አድባራት ቅድስት ማርያም ጽዮን",
                    latitude = 14.1299,
                    longitude = 38.7189,
                    region = "Axum, Tigray",
                    diocese = "Axum Patriarchal Diocese",
                    churchType = "CATHEDRAL",
                    description = "The spiritual heart of the Ethiopian Orthodox Tewahedo Church, repository of the Ark of the Covenant.",
                    history = "Founded in the 4th century under King Ezana and Abba Selama (St. Frumentius). Houses the Chapel of the Tablet preserving Tabote Tsiyon.",
                    address = "Old Axum Stelae Field, Axum",
                    contactPhone = "+251 34 775 2210",
                    contactEmail = "axum.tsiyon@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 3,
                    name = "Debre Birhan Selassie Church",
                    nameAmharic = "ደብረ ብርሃን ሥላሴ",
                    latitude = 12.6119,
                    longitude = 37.4819,
                    region = "Gondar, Amhara",
                    diocese = "Central Gondar Diocese",
                    churchType = "PARISH",
                    description = "Mountain of the Light of the Trinity, world-renowned for its ceiling adorned with winged angel faces.",
                    history = "Built in 1694 by Emperor Iyasu I. Miraculously defended in 1888 when bees swarmed around its perimeter to repel invaders.",
                    address = "Debre Birhan Hill, Gondar",
                    contactPhone = "+251 58 111 4055",
                    contactEmail = "gondar.selassie@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 4,
                    name = "Biete Medhane Alem",
                    nameAmharic = "ቤተ መድኃኔ ዓለም",
                    latitude = 12.0327,
                    longitude = 39.0436,
                    region = "Lalibela, Amhara",
                    diocese = "North Wollo Diocese",
                    churchType = "ROCK_HEWN",
                    description = "The largest monolithic rock-hewn church in the world, surrounded by 72 exterior columns.",
                    history = "Carved in the 12th century, it houses the sacred 800-year-old golden Afro-Ayigeba cross.",
                    address = "Northern Cluster, Lalibela",
                    contactPhone = "+251 33 336 0035",
                    contactEmail = "medhanealem.lal@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 5,
                    name = "Debre Damo Monastery",
                    nameAmharic = "ደብረ ዳሞ ገዳም",
                    latitude = 14.3756,
                    longitude = 39.2994,
                    region = "Tigray",
                    diocese = "Eastern Tigray Diocese",
                    churchType = "MONASTERY",
                    description = "Ancient 6th-century clifftop monastery accessible only by climbing a 15-meter leather rope (Jiba).",
                    history = "Founded by Abuna Aregawi, one of the Nine Saints. Houses ancient parchment manuscripts and 6th-century timber architecture.",
                    address = "Debre Damo Amba, Tigray",
                    contactPhone = "+251 34 771 9090",
                    contactEmail = "debredamo@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 6,
                    name = "Holy Trinity Cathedral (Kidist Selassie)",
                    nameAmharic = "መንበረ ጸባዖት ቅድስት ሥላሴ ካቴድራል",
                    latitude = 9.0306,
                    longitude = 38.7619,
                    region = "Addis Ababa",
                    diocese = "Addis Ababa Patriarchal See",
                    churchType = "CATHEDRAL",
                    description = "Highest ranking cathedral in Ethiopia and the official seat of the Orthodox Patriarchate.",
                    history = "Consecrated in 1942 to commemorate the liberation of Ethiopia, housing imperial tombs and monument carvings.",
                    address = "Arat Kilo, Addis Ababa",
                    contactPhone = "+251 11 123 3450",
                    contactEmail = "patriarchate@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 7,
                    name = "Saint George Cathedral (Arada Giorgis)",
                    nameAmharic = "ገነተ ጽጌ ቅዱስ ጊዮርጊስ ካቴድራል",
                    latitude = 9.0353,
                    longitude = 38.7517,
                    region = "Addis Ababa",
                    diocese = "Addis Ababa Diocese",
                    churchType = "CATHEDRAL",
                    description = "Octagonal neoclassical cathedral commissioned after the historic victory at Adwa in 1896.",
                    history = "Commissioned by Emperor Menelik II with monumental stained glass and murals by Afewerk Tekle.",
                    address = "Piazza / Arada, Addis Ababa",
                    contactPhone = "+251 11 155 7800",
                    contactEmail = "arada.giorgis@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 8,
                    name = "Debre Libanos Monastery",
                    nameAmharic = "ደብረ ሊባኖስ ገዳም",
                    latitude = 9.7167,
                    longitude = 38.8500,
                    region = "North Shewa, Oromia",
                    diocese = "North Shewa Diocese",
                    churchType = "MONASTERY",
                    description = "Historic monastic center founded by Saint Tekle Haymanot in the sheer river gorge.",
                    history = "Established in 1284, serving as the motherhouse of monasticism and the seat of the Echege.",
                    address = "Debre Libanos Gorge, Oromia",
                    contactPhone = "+251 11 890 2200",
                    contactEmail = "debrelibanos@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 9,
                    name = "Ura Kidane Mehret Monastery",
                    nameAmharic = "ኡራ ኪዳነ ምሕረት",
                    latitude = 11.6967,
                    longitude = 37.3233,
                    region = "Lake Tana, Amhara",
                    diocese = "Bahir Dar Diocese",
                    churchType = "MONASTERY",
                    description = "Island peninsula sanctuary famous for its circular architecture and 16th-century vibrant murals.",
                    history = "Founded in the 14th century by Saint Betre Maryam on the Zege Peninsula.",
                    address = "Zege Peninsula, Lake Tana",
                    contactPhone = "+251 58 220 1199",
                    contactEmail = "urakidane@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 10,
                    name = "Gishen Debre Kerbe Mariam",
                    nameAmharic = "ግሼን ደብረ ከርቤ ማርያም",
                    latitude = 11.5833,
                    longitude = 39.3167,
                    region = "Ambassel, Wollo",
                    diocese = "South Wollo Diocese",
                    churchType = "MONASTERY",
                    description = "Natural cross-shaped mountain sanctuary preserving the right-wing piece of the True Cross.",
                    history = "Established by Emperor Zara Yaqob in the 15th century as the permanent citadel of the True Cross.",
                    address = "Gishen Mountain, Wollo",
                    contactPhone = "+251 33 111 8844",
                    contactEmail = "gishen.kerbe@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 11,
                    name = "Entoto Saint Mary Church",
                    nameAmharic = "እንጦጦ መንበረ ፀሐይ ቅድስት ማርያም",
                    latitude = 9.0911,
                    longitude = 38.7628,
                    region = "Addis Ababa",
                    diocese = "Addis Ababa Diocese",
                    churchType = "PARISH",
                    description = "Hilltop sanctuary established by Empress Taytu Betul overlooking the capital.",
                    history = "Built in 1882 on the eucalyptus-crested Entoto Ridge, housing imperial artifacts and healing holy water.",
                    address = "Entoto Ridge, Addis Ababa",
                    contactPhone = "+251 11 155 0101",
                    contactEmail = "entoto.mariam@eotc.org",
                    isVerified = true
                ),
                ChurchEntity(
                    id = 12,
                    name = "Wukro Chirkos Rock Church",
                    nameAmharic = "ውቅሮ ቂርቆስ",
                    latitude = 13.7833,
                    longitude = 39.6000,
                    region = "Eastern Tigray",
                    diocese = "Kilte Awulaelo Diocese",
                    churchType = "ROCK_HEWN",
                    description = "Semi-monolithic sandstone basilica dedicated to the infant martyr Saint Cyricus.",
                    history = "Carved in the 4th century during the early Axumite era, featuring barrel vaults and cruciform pillars.",
                    address = "Wukro, Tigray",
                    contactPhone = "+251 34 778 0033",
                    contactEmail = "wukro.chirkos@eotc.org",
                    isVerified = true
                )
            )

            churchDao.insertAll(churches)

            val tabots = listOf(
                // Biete Giyorgis
                TabotEntity(churchId = 1, name = "ቅዱስ ጊዮርጊስ", nameEnglish = "Saint George (Giorgis)", nigsMonth = 8, nigsDay = 23, description = "Martyrdom Feast of Saint George"),
                TabotEntity(churchId = 1, name = "ቅድስት ማርያም", nameEnglish = "Kidist Mariam", nigsMonth = 1, nigsDay = 21, description = "Nativity of Saint Mary"),

                // Axum Tsiyon
                TabotEntity(churchId = 2, name = "ጽላተ ጽዮን", nameEnglish = "Ark of Zion (Tsiyon)", nigsMonth = 3, nigsDay = 21, description = "Hidar Zion - Arrival of the Ark of Covenant"),
                TabotEntity(churchId = 2, name = "እመቤታችን ማርያም", nameEnglish = "Our Lady Mary", nigsMonth = 6, nigsDay = 16, description = "Kidane Mihret Covenant"),

                // Gondar Selassie
                TabotEntity(churchId = 3, name = "ቅድስት ሥላሴ", nameEnglish = "Holy Trinity (Selassie)", nigsMonth = 5, nigsDay = 7, description = "Tir Selassie Holy Feast"),
                TabotEntity(churchId = 3, name = "ቅዱስ ገብርኤል", nameEnglish = "Archangel Gabriel", nigsMonth = 4, nigsDay = 19, description = "Tahsas Gabriel Feast"),

                // Medhane Alem Lalibela
                TabotEntity(churchId = 4, name = "መድኃኔ ዓለም", nameEnglish = "Medhane Alem (Savior of the World)", nigsMonth = 2, nigsDay = 27, description = "Tikimt Medhane Alem Feast"),
                TabotEntity(churchId = 4, name = "አሥራ ሁለቱ ሐዋርያት", nameEnglish = "Twelve Apostles", nigsMonth = 11, nigsDay = 5, description = "Hamle Apostles Feast"),

                // Debre Damo
                TabotEntity(churchId = 5, name = "አቡነ አረጋዊ", nameEnglish = "Abuna Aregawi", nigsMonth = 2, nigsDay = 14, description = "Ascension of Abuna Aregawi"),

                // Kidist Selassie Addis
                TabotEntity(churchId = 6, name = "ቅድስት ሥላሴ", nameEnglish = "Holy Trinity (Selassie)", nigsMonth = 11, nigsDay = 7, description = "Hamle Selassie National Feast"),
                TabotEntity(churchId = 6, name = "ቅዱስ ሚካኤል", nameEnglish = "Archangel Michael", nigsMonth = 3, nigsDay = 12, description = "Hidar Michael Commemoration"),

                // Arada Giorgis Addis
                TabotEntity(churchId = 7, name = "ቅዱስ ጊዮርጊስ", nameEnglish = "Saint George (Giorgis)", nigsMonth = 8, nigsDay = 23, description = "Adwa Memorial Feast of Saint George"),

                // Debre Libanos
                TabotEntity(churchId = 8, name = "አቡነ ተክለ ሃይማኖት", nameEnglish = "Saint Tekle Haymanot", nigsMonth = 12, nigsDay = 24, description = "Nehase Feast of Saint Tekle Haymanot"),

                // Ura Kidane Mehret
                TabotEntity(churchId = 9, name = "ኪዳነ ምሕረት", nameEnglish = "Kidane Mihret (Covenant of Mercy)", nigsMonth = 6, nigsDay = 16, description = "Yekatit Kidane Mihret Annual Feast"),

                // Gishen Maryam
                TabotEntity(churchId = 10, name = "መስቀለ ክርስቶስ", nameEnglish = "Holy Cross of Christ", nigsMonth = 1, nigsDay = 21, description = "Meskerem Gishen Meskel Feast"),

                // Entoto Maryam
                TabotEntity(churchId = 11, name = "ቅድስት ማርያም", nameEnglish = "Saint Mary (Mariam)", nigsMonth = 3, nigsDay = 21, description = "Hidar Zion Blessing on Entoto"),
                TabotEntity(churchId = 11, name = "ቅዱስ ራጉኤል", nameEnglish = "Archangel Raguel", nigsMonth = 1, nigsDay = 1, description = "Enkutatash Archangel Raguel"),

                // Wukro Chirkos
                TabotEntity(churchId = 12, name = "ቅዱስ ቂርቆስ", nameEnglish = "Saint Cyricus (Chirkos)", nigsMonth = 5, nigsDay = 15, description = "Tir Chirkos Martyr Feast")
            )

            tabotDao.insertAll(tabots)

            // Seed Live Gubae Events
            val now = System.currentTimeMillis()
            val threeDaysAhead = now + (3 * 24 * 60 * 60 * 1000L)
            val fiveDaysAhead = now + (5 * 24 * 60 * 60 * 1000L)
            val pastDate = now - (14 * 24 * 60 * 60 * 1000L)

            val gubaeEvents = listOf(
                GubaeEventEntity(
                    churchId = 1,
                    title = "National Saint George Pilgrim Gubae",
                    description = "Continuous 3-day spiritual chanting, liturgical zema, and blessing of pilgrims around Biete Giyorgis.",
                    startDateEpoch = now - (12 * 60 * 60 * 1000L),
                    endDateEpoch = threeDaysAhead,
                    isActive = true
                ),
                GubaeEventEntity(
                    churchId = 6,
                    title = "Patriarchal Youth Spiritual Conference",
                    description = "Special gathering at Holy Trinity Cathedral featuring theological lectures and night vigil.",
                    startDateEpoch = now - (6 * 60 * 60 * 1000L),
                    endDateEpoch = fiveDaysAhead,
                    isActive = true
                ),
                GubaeEventEntity(
                    churchId = 2,
                    title = "Axum Tsiyon Memorial Vigil",
                    description = "Commemoration of ancient Axumite scholars and historical hymns.",
                    startDateEpoch = pastDate - (2 * 24 * 60 * 60 * 1000L),
                    endDateEpoch = pastDate,
                    isActive = false
                )
            )

            gubaeDao.insertAll(gubaeEvents)

            // Seed initial favorites
            favoriteDao.add(FavoriteEntity(churchId = 1))
            favoriteDao.add(FavoriteEntity(churchId = 2))
            favoriteDao.add(FavoriteEntity(churchId = 8))
        }
    }
}
