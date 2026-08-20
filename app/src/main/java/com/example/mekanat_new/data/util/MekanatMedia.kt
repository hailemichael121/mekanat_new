package com.example.mekanat_new.data.util

object MekanatMedia {
    fun getChurchImageUrl(churchId: Long): String {
        return when (churchId) {
            1L -> "https://images.unsplash.com/photo-1590059390046-248792036733?auto=format&fit=crop&w=1000&q=80" // Biete Giyorgis Lalibela
            2L -> "https://images.unsplash.com/photo-1548625361-19598284724b?auto=format&fit=crop&w=1000&q=80" // Axum Tsiyon
            3L -> "https://images.unsplash.com/photo-1565011523534-747a8601f10a?auto=format&fit=crop&w=1000&q=80" // Debre Birhan Selassie
            4L -> "https://images.unsplash.com/photo-1590059390046-248792036733?auto=format&fit=crop&w=1000&q=80" // Medhane Alem Lalibela
            5L -> "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1000&q=80" // Debre Damo
            6L -> "https://images.unsplash.com/photo-1548625361-19598284724b?auto=format&fit=crop&w=1000&q=80" // Holy Trinity Cathedral
            7L -> "https://images.unsplash.com/photo-1565011523534-747a8601f10a?auto=format&fit=crop&w=1000&q=80" // Saint George Arada
            8L -> "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1000&q=80" // Debre Libanos
            9L -> "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1000&q=80" // Ura Kidane Mehret
            10L -> "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=1000&q=80" // Gishen Maryam
            11L -> "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=1000&q=80" // Entoto Maryam
            12L -> "https://images.unsplash.com/photo-1590059390046-248792036733?auto=format&fit=crop&w=1000&q=80" // Wukro Chirkos
            else -> "https://images.unsplash.com/photo-1548625361-19598284724b?auto=format&fit=crop&w=1000&q=80"
        }
    }

    fun getTabotImageUrl(tabotNameEnglish: String): String {
        return when {
            tabotNameEnglish.contains("George", ignoreCase = true) || tabotNameEnglish.contains("Giorgis", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?auto=format&fit=crop&w=600&q=80"
            tabotNameEnglish.contains("Zion", ignoreCase = true) || tabotNameEnglish.contains("Tsiyon", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1548625361-19598284724b?auto=format&fit=crop&w=600&q=80"
            tabotNameEnglish.contains("Mary", ignoreCase = true) || tabotNameEnglish.contains("Mariam", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?auto=format&fit=crop&w=600&q=80"
            tabotNameEnglish.contains("Trinity", ignoreCase = true) || tabotNameEnglish.contains("Selassie", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1565011523534-747a8601f10a?auto=format&fit=crop&w=600&q=80"
            tabotNameEnglish.contains("Medhane", ignoreCase = true) || tabotNameEnglish.contains("Savior", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1544816155-12df9643f363?auto=format&fit=crop&w=600&q=80"
            tabotNameEnglish.contains("Aregawi", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=600&q=80"
            tabotNameEnglish.contains("Tekle", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=600&q=80"
            tabotNameEnglish.contains("Cross", ignoreCase = true) || tabotNameEnglish.contains("Meskel", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=600&q=80"
            else ->
                "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?auto=format&fit=crop&w=600&q=80"
        }
    }
}
