#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

// Set these to run example.
#define FIREBASE_HOST "ayushya-bb2e9.firebaseio.com"
#define FIREBASE_AUTH "ERsIYLheZyFBK2jXoYdA1HcYeNtp3o4zXNw5jg6y"
#define WIFI_SSID "Deepblue"
#define WIFI_PASSWORD "asdfghjkl"
String patient_id="";
String device_no="1";
void setup() {
  Serial.begin(9600);
  pinMode(D0,OUTPUT);
  pinMode(D2,INPUT);
  digitalWrite(D0,HIGH);
  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.setString("/devices/"+device_no+"/patient_id","");
  while(patient_id==""){
    patient_id = Firebase.getString("/devices/"+device_no+"/patient_id");
  }
  Firebase.setString("/patients/"+patient_id+"/current_test","n/a");
  Firebase.setString("/patients/"+patient_id+"/specific_test/name","n/a");
  Firebase.setString("/patients/"+patient_id+"/specific_test/value","");
}

void loop() {
  String Test = Firebase.getString("/patients/"+patient_id+"/current_test");
  
  // full check up
  if(Test == "Temperature"){
    float temp = 34.0;
      digitalWrite(D0,LOW);
      delay(1000);
      Firebase.setString("/patients/"+patient_id+"/tests/temperature",String(temp));
      delay(20);
      digitalWrite(D0,HIGH);
      Firebase.setString("/patients/"+patient_id+"/current_test","Done");
  }
  else if(Test == "Cough"){
      digitalWrite(D0,LOW);
      delay(1000);
      Firebase.setString("/patients/"+patient_id+"/tests/coughi","MODERATE");
      delay(20);
      digitalWrite(D0,HIGH);
      Firebase.setString("/patients/"+patient_id+"/current_test","dOne");
  }
  // specific_test results
  String Specific_Test = Firebase.getString("/patients/"+patient_id+"/specific_test/name");
  if(Test == "n/a" && Specific_Test == "Temperature"){
    float temp = 39.0;
      digitalWrite(D0,LOW);
      delay(1000);
      Firebase.setString("/patients/"+patient_id+"/specific_test/value",String(temp));
      delay(20);
      digitalWrite(D0,HIGH);
      Firebase.setString("/patients/"+patient_id+"/specific_test/name","done");
  }
  else if(Test == "n/a" && Specific_Test == "Cough"){
      digitalWrite(D0,LOW);
      delay(1000);
      Firebase.setString("/patients/"+patient_id+"/specific_test/value","MODERATE");
      delay(20);
      digitalWrite(D0,HIGH);
      Firebase.setString("/patients/"+patient_id+"/specific_test/name","done");
  }
}
