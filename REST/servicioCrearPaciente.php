<?php
# incorporamos el archivo config.php
include('config.php');
include('administradorBD.php');

$db = new administradorBD();

if($_SERVER['REQUEST_METHOD']=='POST'){
  
  #Get all the variables inserted
  $Matricula = $_POST['Matricula'];
  $Password = $_POST['Password'];
  $Nombre =  $_POST['Nombre'];
  $ApellidoPaterno =  $_POST['ApellidoPaterno'];
  $ApellidoMaterno =  $_POST['ApellidoMaterno'];
  $Edad =  $_POST['Edad'];
  $NombreContactoEmergencia =  $_POST['NombreContactoEmergencia'];
  $NumContactoEmergencia =  $_POST['NumContactoEmergencia'];

  $Padecimientos =  $_POST['Padecimientos'];
  $Medicamentos =  $_POST['Medicamentos'];
  
  #Query for inserting the patient
  $query1 = "INSERT INTO Pacientes(Matricula, Nombre, ApellidoPaterno, ApellidoMaterno, Edad, NombreContactoEmergencia, NumContactoEmergencia) ";
  $query1 .= "VALUES ('$Matricula','$Nombre','$ApellidoPaterno','$ApellidoMaterno',$Edad,'$NombreContactoEmergencia',$NumContactoEmergencia)";

  #Query for inserting the loging information for the patient
  $query2 = "INSERT INTO Login(Pacientes_Matricula,Password) VALUES ('$Matricula','$Password')";

  #Query for creating the medical history of the patient
  $query3 = "INSERT INTO HistorialMedico(Pacientes_Matricula,Padecimientos,Medicamentos) VALUES ('$Matricula','$Padecimientos','$Medicamentos')";
  
  #Execute all three queries
  $res1 = $db->executeQuery($query1);
  $res2 = $db->executeQuery($query2);
  $res3 = $db->executeQuery($query3);

  $response = array();
  
  #Result of query 1
  if($res1){
    #Result of query 2
    if($res2){
        #Result of query 3
        if($res3){

            #Success
            header('Content-type: application/json');
            $response['success'] = true;
            echo json_encode($response);
        }
        #If query 3 failed
        else{
            $response['Codigo'] = 6;
            $response['Mensaje'] = "Fallo insertar el historial medico del paciente";
    
            echo json_encode($response);
        }
    }
    #If query 2 failed
    else{
        $response['Codigo'] = 5;
        $response['Mensaje'] = "Fallo insertar el login del paciente";

        echo json_encode($response);
    }
  }
  #If query 1 failed
  else{
    $response['Codigo'] = 4;
    $response['Mensaje'] = "Fallo insertar el paciente";

    echo json_encode($response);
 }

}
?>