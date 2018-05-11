## Käyttöohje

Lataa tiedosto [VerkkoVisualisoija.jar](https://github.com/Laakeri/otm-harjoitustyo/releases/tag/viikko6)

# Ohjelman käynnistäminen

Ohjelma käynnistetään komennolla

```
java -jar VerkkoVisualisoija.jar
```

# Ohjelman käyttäminen

Avaa tiedosto -toiminnolla voit lukea verkon kuvauksen tiedostosta. Yksi tuetuista tiedostomuodoista näyttää tältä:
```
e: a b
e: a c
e: a d
e: b c
e: b d
e: c d
e: d e
```
Jokainen rivi siis kuvaa yhden kaaren verkossa. Verkon solmujen nimet voivat olla mitä tahansa merkkijonoja.

Verkon solmuja voi raahata hiirellä ja verkkoa voi editoida ohjelmassa. Editointitoimintojen käyttöä auttaa GUIssa näkyvä teksti.

Kun verkon tallentaa ohjelmalla, sen tiedostoon tallennetaan myös solmujen asettelu näytölle. Tiedostojen avaamistoiminto toimii älykkäästi ja tunnistaa tiedostomuodon ja että onko siihen tallennettu asettelun tietoja, eli käyttäjän ei tarvitse huolehtia eri tiedostomuodoista avatessa tiedostoja, kunhan ohjelma tukee avattavaa muotoa.
