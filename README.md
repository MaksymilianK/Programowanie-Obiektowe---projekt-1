# World simulator

Projekt 1 z PO - Konrad Kowalczyk

Prowadzący laboratoria: mgr. Zbigniew Kaleta

gr. środa

## 1. Wstęp
Program napisany został w Javie 15. Korzysta z narzędzia do budowania [Gradle](https://gradle.org/) (wersja 6.7.1 jest
wystarczająca).

## 2. Uruchomienie
Aby uruchomić program należy pobrać jego kod źródłowy wraz ze wszystkimi plikami znajdującymi się w tym repozytorium.
Następnie uruchomić w głównym folderze projektu wiersz poleceń i wpisać komendę ```gradlew run```.

## 3. Używane biblioteki
Projekt używa biblioteki [Jackson](https://github.com/FasterXML/jackson) do parsowania JSONa. Testy napisane są
z użyciem [JUnit 5](https://junit.org/junit5/), biblioteki [AssertJ](https://assertj.github.io/doc) oraz biblioteki
[Mockito](https://site.mockito.org/). Interfejs graficzny działa w oparciu o [JavaFX 15](https://openjfx.io/).

## 4. Komentarze
Starałem się, żeby grafika programu wyglądała przyzwoicie. Stąd decyzja o reprezentowaniu zwierząt i roślin przez
ładne ikony zamiast prostych kółek i kwadratów. Pociągnęło to za sobą konieczność ustalenia minimalnej szerokości
pola mapy (25px), gdyż poniżej tej szerokości ikony stają się zbyt małe. To z kolei pociągnęło za sobą konieczność
wyświetlania mapy z paskami przewijania w pionie i w poziomie. Podobne paski przewijania pojawiają się dla interfejsu
użytkownika, gdyż chciałem upewnić się, że na pewno zostanie on poprawnie wyświetlony na każdym ekranie.

Ten sposób wyświetlania mapy ma swoją cenę, szczególnie przy wyświetlaniu podwójnej symulacji. Ponieważ każda mapa
składa się z 3 warstw (pola, zwierzęta i rośliny) oraz każda jest przewijana (może mieć bardzo dużą szerokość,
wielokrotnie większą od ekranu), zajmuje bardzo dużo miejsca w pamięci karty graficznej. Z moich testów wynika jednak,
że jest w stanie wyświetlić mapę 150x150. Powyżej tej wielkości może wystąpić błąd, spowodowany brakiem pamięci VRAM.

Kolejną kwestią wartą skomentowania jest sposób obliczania powierzchni dżungli. Pozwoliłem sobie z powodów estetycznych
na ustalenie dodatkowej zasady, że dżungla musi znajdować się idealnie na środku mapy. Tzn. na przykład dla mapy
```7x15``` i ```jungleRatio=0.2``` idealna byłaby dżungla 4x8. Ale ponieważ 7 i 15 są liczbami nieparzystymi, a 4 i 8
parzystymi, dżungla nie mogłaby znajdować sie idealnie na środku mapy. Dlatego mój algorytm ustali dżunglę na 5x7.
Innymi słowy, algorytm dobiera taką szerokość i wysokość dżungli, żeby dało się ja umieścić idealnie na środku i żeby
proporcja powierzchni dżungli do powierzchni mapy był najbliższy ```jungleRatio```.