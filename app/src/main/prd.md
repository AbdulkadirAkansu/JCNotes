


Projenin en önemli kuralları : 



En önemli kural : 
-Benden izinsiz klasör oluşturamazsın veya sınıf oluşturamazsın izin izteyeceksin veya ben söylersem oluşturacaksın.

1.Navigasyon işlemleri NavGraph.kt yapılacaktır.
2.Screen sayfalarında sadece Screen ile alakalı bölümler olacaktır data ile alakalı kodlar viewmodel eklenecektir.
3.Her sayfaya ait viewmodel oluşturulacak ve orada view harici kodlar orada yazılacaktır.
4.MainActivity sayfasında sadece başlangıç sayfası olacak extra gereksiz bir kod yazılmayacaktır.
5.Klasör ve mimariye dikkat edilmesi gerekmektedir.
6.Aynı isimden 2 adet sınıf olamaz özellikle klasörlere dikkatli bakmanı istiyorum.
7.Viewmodelları presentation\viewmodel klasörüne ekleyeceksin
8.Gerekli componentslerin hepsini components\notes ekleyeceksin
9.Her screen klasörünün içinde bunlara dikkat edeceksin örnek home\HomeScreen.kt gibi veya note_add\AddNoteScreen.kt gibi
10.Yaklaşım bu şekilde olmalıdır Veriler Repository -> ViewModel -> UI yönünde akar
Kullanıcı etkileşimleri UI -> ViewModel -> Repository yönünde akar anlıyor musun. Repositoryde olanları viewmodelda kullanırsın screen viewmodeldan çağırırsın bunu özellikle dikkat edeceksin.



