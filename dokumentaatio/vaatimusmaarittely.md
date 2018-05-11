# Vaatimusmäärittely

## Sovelluksen tarkoitus

Sovelluksella voi piirtää verkkoja as in [matemaattisia verkkoja](https://fi.wikipedia.org/wiki/Verkkoteoria) mahdollisimman helposti.
Sovelluksesta olisi apua esim. kun tiedetään millä testisyötteellä tira tehtävän palautus ei toimi ja halutaan nähdä miltä tämä syöte oikeasti näyttää.
Samalla tavalla hyötyä olisi enemmän ammattimaisissa tilanteissa kun verkkoalgoritmeja koodataan ja erilaisia syötteitä tarkastellaan.

## Toiminnallisuudet

* Ohjelma lukee verkon kuvauksen tiedostosta ja piirtää sen jollain tavalla näytölle
* Käyttäjä voi itse asetella verkon solmujen sijainteja näytöllä että kuvasta tulee yleiskatsauksellisempi
* Tiedostoon voidaan tallentaa solmujen sijoittelu näytöllä ja tiedosto voidaan ladata
* Verkon solmuja voi nimetä
* Käyttäjä voi itse muokata verkkoa
* Ohjelma tukee useaa eri tiedostomuotoa
* Hyvä asettelu verkolle lasketaan kokeilemalla useita satunnaisia asetteluja ja valitsemalla paras niistä. Tämä on huono algoritmi isoille verkoille, mutta jos verkon koko on alle 10 solmua se voi toimia ihan hyvin.
