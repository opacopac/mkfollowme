# Mikrokopter FollowMe for Mobile #

## Überblick ##

J2ME Map Client für [OpenStreetMap](http://www.openstreetmap.org/) mit [Mikrokopter](http://www.mikrokopter.de/)-Funktionen. :)

So sieht es aus:

![http://www.tschanz.com/mkfollowme/MKFollowMe_Screenshot.png](http://www.tschanz.com/mkfollowme/MKFollowMe_Screenshot.png)

(rot: MK, blau: Mobile, grün: Waypoints)

## Features ##

  * Interaktive, zoombare Karte ([OpenStreetMap](http://www.openstreetmap.org/)) zeigt eigene und MK-Position an.
  * Follow-me-Funktion für Mobiles mit GPS.
  * Waypoints setzen auf Karte und zum MK senden.
  * Map besitzt 'nen Cache und speichert die letzten Position, damit das Datenabo geschont wird.

## Voraussetzungen ##

  * Die Applikation wurde in Java ME (Micro Edition) programmiert und setzt auf MIDP 2.0 / CLDC 1.1 auf. Zusätzlich werden optional die Pakete JSR-179 (für GPS) und JSR-82 (für Bluetooth) unterstützt.
  * Der Map besitzt zwar nen Cache und einmal angesehene Kartenausschnitte bleiben auch nach Beenden des Programs gespeichert, trotzdem ist ein vernünftiges Datenabo zu empfehlen!