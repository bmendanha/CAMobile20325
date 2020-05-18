package com.brunomendanha.googlemap

class Position (var lat: Double, var lng: Double )

class BikeStation(var number: Int, var address: String, var position: Position)

class Stations(val stations: List<BikeStation>)

/**
 * This app is part of an assessment of the module Mobile Applications in Dorset College
 * ID Student: 20325
 * Name: Bruno H. M. Mendanha
 */
