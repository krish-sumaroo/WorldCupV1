package com.competition.worldcupv1.utils;

import com.example.worldcupv1.R;

public class CountryFlagHelper {
	public static int getFlagId(String countryCode){
		int flagId = -1;
		
		if(countryCode.equals("URU")){
			flagId = R.drawable.flag_uruguay;
		}else if(countryCode.equals("ESP")){
			flagId = R.drawable.flag_spain;
		}
		
		// to be uncommented when img is done
		/* else if(countryCode.equals("DEU")){
			flagId = R.drawable.flag_germany ;
		}else if(countryCode.equals("ARG")){
			flagId = R.drawable.flag_argentina;
		}else if(countryCode.equals("COL")){
			flagId = R.drawable.flag_colombia;
		}else if(countryCode.equals("BEL")){
			flagId = R.drawable.flag_Belgium;
		}else if(countryCode.equals("BRA")){
			flagId = R.drawable.flag_brazil;
		}else if(countryCode.equals("CHE")){
			flagId = R.drawable.flag_switzerland;
		}else if(countryCode.equals("CIV")){
			flagId = R.drawable.flag_ivory_coast;
		}else if(countryCode.equals("GHA")){
			flagId = R.drawable.flag_ghana;
		}else if(countryCode.equals("DZA")){
			flagId = R.drawable.flag_algeria;
		}else if(countryCode.equals("NGA")){
			flagId = R.drawable.flag_nigeria;
		}else if(countryCode.equals("CMR")){
			flagId = R.drawable.flag_cameroon;
		}else if(countryCode.equals("CHL")){
			flagId = R.drawable.flag_chile;
		}else if(countryCode.equals("ECU")){
			flagId = R.drawable.flag_ecuador;
		}else if(countryCode.equals("JPN")){
			flagId = R.drawable.flag_japan;
		}else if(countryCode.equals("IRN")){
			flagId = R.drawable.flag_iran;
		}else if(countryCode.equals("KOR")){
			flagId = R.drawable.flag_south_korea;
		}else if(countryCode.equals("AUS")){
			flagId = R.drawable.flag_australia;
		}else if(countryCode.equals("USA")){
			flagId = R.drawable.flag_united_states;
		}else if(countryCode.equals("MEX")){
			flagId = R.drawable.flag_mexico;
		}else if(countryCode.equals("CRI")){
			flagId = R.drawable.flag_costa_rica;
		}else if(countryCode.equals("HND")){
			flagId = R.drawable.flag_honduras;
		}else if(countryCode.equals("BIH")){
			flagId = R.drawable.flag_bosnia_herzegovina;
		}else if(countryCode.equals("CRO")){
			flagId = R.drawable.flag_croatia;
		}else if(countryCode.equals("ENG")){
			flagId = R.drawable.flag_england;
		}else if(countryCode.equals("FRA")){
			flagId = R.drawable.flag_france;
		}else if(countryCode.equals("GRE")){
			flagId = R.drawable.flag_greece;
		}else if(countryCode.equals("ITA")){
			flagId = R.drawable.flag_italy;
		}else if(countryCode.equals("NED")){
			flagId = R.drawable.flag_netherlands;
		}else if(countryCode.equals("POR")){
			flagId = R.drawable.flag_portugal;
		}else if(countryCode.equals("RUS")){
			flagId = R.drawable.flag_russia;
		}*/
		else{
			flagId = R.drawable.defaut_bg;
		}
		return flagId;
	}
	
	public static float getCountryNameTextSize(String countryName){
		
		float textSize = 23.0f;
		
		if(countryName.length()>6 ){
			textSize = 19.0f;
		}
		return textSize;
	}
}
