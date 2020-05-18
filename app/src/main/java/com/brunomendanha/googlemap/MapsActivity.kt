/**
 * This app is part of an assessment of the module Mobile Applications in Dorset College
 * ID Student: 20325
 * Name: Bruno H. M. Mendanha
 */

package com.brunomendanha.googlemap

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var listOfBikeStations: List<BikeStation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        retrieveFavourites()
    }

    override fun onResume() {
        super.onResume()
        Log.i(getString(R.string.MAPLOGGING), "onResume")

        retrieveFavourites()
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Only fetch data when map is ready

        getBikeStationJsonData()
        setMarkerListener()

    }

    fun getBikeStationJsonData() {
        Log.i(getString(R.string.MAPLOGGING), "Loading JSON data")

        var url = getString(R.string.DUBLIN_BIKE_API_URL) + getString(R.string.DUBLIN_BIKE_API_KEY)

        Log.i(getString(R.string.MAPLOGGING), url)

        //  a request object

        val request = Request.Builder().url(url).build()

        // Create a client

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // TODO("Not yet implemented")

                Log.i(getString(R.string.MAPLOGGING), "http fail")
            }

            override fun onResponse(call: Call, response: Response) {
                //  TODO("Not yet implemented")

                Log.i(getString(R.string.MAPLOGGING), "http success")

                // Get the response body
                val body = response?.body?.string()


                Log.i(getString(R.string.MAPLOGGING), body)

                // Create a json builder object
                val gson = GsonBuilder().create()

                listOfBikeStations = gson.fromJson(body, Array<BikeStation>::class.java).toList()

                renderListOfBikeStationMarkers()
            }

        })
    }

    fun renderListOfBikeStationMarkers() {

        runOnUiThread {

            listOfBikeStations.forEach {
                val position = LatLng(it.position.lat, it.position.lng)
                var marker1 =
                    mMap.addMarker(
                        MarkerOptions().position(position).title("Marker in ${it.address}")
                    )
                marker1.setTag(it.number)
                Log.i(getString(R.string.MAPLOGGING), it.address)
            }

            val centreLocation = LatLng(53.349562, -6.278198)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centreLocation, 16.0f))
        }
    }


    fun setMarkerListener() {

        mMap.setOnMarkerClickListener { marker ->
            Log.i(getString(R.string.MAPLOGGING), getString(R.string.MAKERCLICKED))

            Log.i(
                getString(R.string.MAPLOGGING), "Marker id (tag) is " + marker.getTag().toString()
            )
            if (marker.isInfoWindowShown) {

                marker.hideInfoWindow()
            } else {

                marker.showInfoWindow()
            }

            // Pass in data and add a layout
            val intent = Intent(this, StationActivity::class.java)

           intent.putExtra("lat", marker.position.latitude.toString())
           intent.putExtra("id", marker.getTag().toString())
           intent.putExtra("lng", marker.position.longitude.toString())
            startActivity(intent)

            true
        }

    }

    fun addMarkers() {
        val Christchurch = LatLng(53.343368, -6.27012)
        var marker1 = mMap.addMarker(MarkerOptions().position(Christchurch).title("Marker in Christchurch"))

        // List Example
        listOfBikeStations = listOf(
        BikeStation(42, "Smithfield North", Position(53.349562, -6.278198)),
        BikeStation(30, "Parnell Square North", Position(53.353462, -6.265305)),
        BikeStation(54, "Clonmel Street", Position(53.336021, -6.26298)),
        BikeStation(108, "Avondale Road", Position(53.359405, -6.276142)),
        BikeStation(56, "Mount Street Lower", Position(53.33796, -6.24153)),
        BikeStation(6, "Christchurch Place", Position(53.343368, -6.27012)),
        BikeStation(18, "Grantham Street", Position(53.334123, -6.265436)),
        BikeStation(32, "Pearse Street", Position(53.344304, -6.250427)),
        BikeStation(52, "York Street East", Position(53.338755, -6.262003)),
        BikeStation(48, "Excise Walk", Position(53.347777, -6.244239)),
        BikeStation(13, "Fitzwilliam Square West", Position(53.336074, -6.252825)),
        BikeStation(43, "Portobello Road", Position(53.330091, -6.268044)),
        BikeStation(81, "St. James Hospital (Central)", Position(53.339983, -6.295594)),
        BikeStation(31, "Parnell Street", Position(53.350929, -6.265125)),
        BikeStation(98, "Frederick Street South", Position(53.341515, -6.256853)),
        BikeStation(23, "Custom House", Position(53.348279, -6.254662)),
        BikeStation(106, "Rathdown Road", Position(53.35893, -6.280337)),
        BikeStation(112, "North Circular Road (O'Connell's)", Position(53.357841, -6.251557)),
        BikeStation(68, "Hanover Quay", Position(53.344115, -6.237153)),
        BikeStation(74, "Oliver Bond Street", Position(53.343893, -6.280531)),
        BikeStation(87, "Collins Barracks Museum", Position(53.347477, -6.28525)),
        BikeStation(84, "Brookfield Road", Position(53.339005, -6.300217)),
        BikeStation(90, "Benson Street", Position(53.344153, -6.233451)),
        BikeStation(11, "Earlsfort Terrace", Position(53.334019, -6.258371)),
        BikeStation(17, "Golden Lane", Position(53.340803, -6.267732)),
        BikeStation(45, "Deverell Place", Position(53.351464, -6.255265)),
        BikeStation(114, "Wilton Terrace (Park)", Position(53.333653, -6.248345)),
        BikeStation(72, "John Street West", Position(53.343105, -6.277167)),
        BikeStation(63, "Fenian Street", Position(53.341428, -6.24672)),
        BikeStation(113, "Merrion Square South", Position(53.338614, -6.248606)),
        BikeStation(91, "South Dock Road", Position(53.341833, -6.231291)),
        BikeStation(99, "City Quay", Position(53.346637, -6.246154)),
        BikeStation(9, "Exchequer Street", Position(53.343034, -6.263578)),
        BikeStation(67, "The Point", Position(53.346867, -6.230852)),
        BikeStation(116, "Broadstone", Position(53.3547, -6.272314)),
        BikeStation(55, "Hatch Street", Position(53.33403, -6.260714)),
        BikeStation(62, "Lime Street", Position(53.346026, -6.243576)),
        BikeStation(5, "Charlemont Street", Position(53.330662, -6.260177)),
        BikeStation(97, "Kilmainham Gaol", Position(53.342113, -6.310015)),
        BikeStation(61, "Hardwicke Place", Position(53.357043, -6.263232)),
        BikeStation(77, "Wolfe Tone Street", Position(53.348875, -6.267459)),
        BikeStation(73, "Francis Street", Position(53.342081, -6.275233)),
        BikeStation(4, "Greek Street", Position(53.346874, -6.272976)),
        BikeStation(49, "Guild Street", Position(53.347932, -6.240928)),
        BikeStation(19, "Herbert Place", Position(53.334432, -6.245575)),
        BikeStation(7, "High Street", Position(53.343565, -6.275071)),
        BikeStation(102, "Western Way", Position(53.354929, -6.269425)),
        BikeStation(38, "Talbot Street", Position(53.350974, -6.25294)),
        BikeStation(53, "Newman House", Position(53.337132, -6.26059)),
        BikeStation(58, "Sir Patrick's Dun", Position(53.339218, -6.240642)),
        BikeStation(66, "New Central Bank", Position(53.347122, -6.234749)),
        BikeStation(104, "Grangegorman Lower (Central)", Position(53.355173, -6.278424)),
        BikeStation(101, "King Street North", Position(53.350291, -6.273507)),
        BikeStation(115, "Killarney Street", Position(53.354845, -6.247579)),
        BikeStation(47, "Herbert Street", Position(53.335742, -6.24551)),
        BikeStation(117, "Hanover Quay East", Position(53.343653, -6.231755)),
        BikeStation(8, "Custom House Quay", Position(53.347884, -6.248048)),
        BikeStation(27, "Molesworth Street", Position(53.341288, -6.258117)),
        BikeStation(16, "Georges Quay", Position(53.347508, -6.252192)),
        BikeStation(96, "Kilmainham Lane", Position(53.341805, -6.305085)),
        BikeStation(82, "Mount Brown", Position(53.341645, -6.29719)),
        BikeStation(76, "Market Street South", Position(53.342296, -6.287661)),
        BikeStation(71, "Kevin Street", Position(53.337757, -6.267699)),
        BikeStation(79, "Eccles Street East", Position(53.358115, -6.265601)),
        BikeStation(69, "Grand Canal Dock", Position(53.342638, -6.238695)),
        BikeStation(25, "Merrion Square East", Position(53.339434, -6.246548)),
        BikeStation(51, "York Street West", Position(53.339334, -6.264699)),
        BikeStation(37, "St. Stephen's Green South", Position(53.337494, -6.26199)),
        BikeStation(59, "Denmark Street Great", Position(53.35561, -6.261397)),
        BikeStation(95, "Royal Hospital", Position(53.343897, -6.29706)),
        BikeStation(94, "Heuston Station (Car Park)", Position(53.346985, -6.297804)),
        BikeStation(105, "Grangegorman Lower (North)", Position(53.355954, -6.278378)),
        BikeStation(36, "St. Stephen's Green East", Position(53.337824, -6.256035)),
        BikeStation(93, "Heuston Station (Central)", Position(53.346603, -6.296924)),
        BikeStation(22, "Townsend Street", Position(53.345922, -6.254614)),
        BikeStation(50, "George's Lane", Position(53.35023, -6.279696)),
        BikeStation(110, "Phibsborough Road", Position(53.356307, -6.273717)),
        BikeStation(12, "Eccles Street", Position(53.359246, -6.269779)),
        BikeStation(34, "Portobello Harbour", Position(53.330362, -6.265163)),
        BikeStation(78, "Mater Hospital", Position(53.359967, -6.264828)),
        BikeStation(2, "Blessington Street", Position(53.356769, -6.26814)),
        BikeStation(75, "James Street", Position(53.343456, -6.287409)),
        BikeStation(111, "Mountjoy Square East", Position(53.356717, -6.256359)),
        BikeStation(26, "Merrion Square West", Position(53.339764, -6.251988)),
        BikeStation(65, "Convention Centre", Position(53.34744, -6.238523)),
        BikeStation(15, "Hardwicke Street", Position(53.355473, -6.264423)),
        BikeStation(86, "Parkgate Street", Position(53.347972, -6.291804)),
        BikeStation(10, "Dame Street", Position(53.344007, -6.266802)),
        BikeStation(100, "Heuston Bridge (South)", Position(53.347106, -6.292041)),
        BikeStation(24, "Cathal Brugha Street", Position(53.352149, -6.260533)),
        BikeStation(64, "Sandwith Street", Position(53.345203, -6.247163)),
        BikeStation(109, "Buckingham Street Lower", Position(53.353331, -6.249319)),
        BikeStation(85, "Rothe Abbey", Position(53.338776, -6.30395)),
        BikeStation(107, "Charleville Road", Position(53.359157, -6.281866)),
        BikeStation(33, "Princes Street / O'Connell Street", Position(53.349013, -6.260311)),
        BikeStation(44, "Upper Sherrard Street", Position(53.358437, -6.260641)),
        BikeStation(89, "Fitzwilliam Square East", Position(53.335211, -6.2509)),
        BikeStation(57, "Grattan Street", Position(53.339629, -6.243778)),
        BikeStation(80, "St James Hospital (Luas)", Position(53.341359, -6.292951)),
        BikeStation(41, "Harcourt Terrace", Position(53.332763, -6.257942)),
        BikeStation(3, "Bolton Street", Position(53.351182, -6.269859)),
        BikeStation(40, "Jervis Street", Position(53.3483, -6.266651)),
        BikeStation(29, "Ormond Quay Upper", Position(53.346057, -6.268001)),
        BikeStation(103, "Grangegorman Lower (South)", Position(53.354663, -6.278681)),
        BikeStation(28, "Mountjoy Square West", Position(53.356299, -6.258586)),
        BikeStation(39, "Wilton Terrace", Position(53.332383, -6.252717)),
        BikeStation(83, "Emmet Road", Position(3.347802, -6.292432)),
        BikeStation(21, "Leinster Street South", Position(53.34218, -6.254485)),
        BikeStation(88, "Blackhall Place", Position(53.3488, -6.281637))
        )

        listOfBikeStations.forEach {
            val position = LatLng(it.position.lat, it.position.lng)
            var marker1 = mMap.addMarker(MarkerOptions().position(position).title("Marker in ${it.address}"))
            marker1.setTag(it.number)
            Log.i(getString(R.string.MAPLOGGING), it.address)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Christchurch, 16.0f))

    }

    fun retrieveFavourites() {
        Log.i(getString(R.string.MAPLOGGING), "Marker Preferences are loaded")
        var prefs = getSharedPreferences("com.brunomendanha.googlemap", Context.MODE_PRIVATE)
        var markers = prefs.getStringSet("stationmarkers", setOf())?.toMutableSet()

       markers?.forEach{m ->  Log.i(getString(R.string.MAPLOGGING), "Favourite Marker: ${m}")}

    }
}





