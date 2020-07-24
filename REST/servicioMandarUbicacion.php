<?php
# incorporamos el archivo config.php
include('config.php');
include('administradorBD.php');

$db = new administradorBD();

if($_SERVER['REQUEST_METHOD']=='POST'){
  
    #Get all the variables inserted
    $Matricula = $_POST['Matricula'];
    $Longitud = $_POST['Longitud'];
    $Latitud =  $_POST['Latitud'];

    #Query for searching for the last location of the patient
    $query1 = "SELECT Longitud, Latitud FROM Ubicaciones WHERE Pacientes_Matricula='$Matricula'";

    #Execute the first query
    $result = $db->executeQuery($query1);

    #If a location was already created for that user
    if(mysqli_num_rows($result)){

        #Query for updating the location
        $query2 = "UPDATE Ubicaciones SET Longitud='$Longitud', Latitud='$Latitud' WHERE Pacientes_Matricula='$Matricula'";

        #Execute the query
        $result2 = $db->executeQuery($query2);

        if($result2){
            #Success
            header('Content-type: application/json');
            $response['success'] = true;
            echo json_encode($response);
        }
        else{
            #Failure
            $response['Codigo'] = 4;
            $response['Mensaje'] = "Fallo actualizar la ubicación";
        
            echo json_encode($response);
        }

    }
    #If no location is found
    else{

        #Query for inserting the location
        $query2 = "INSERT INTO Ubicaciones(Pacientes_Matricula, Longitud, Latitud) VALUES ('$Matricula','$Longitud','$Latitud')";

        #Execute the query
        $result2 = $db->executeQuery($query2);

        if($result2){
            #Success
            header('Content-type: application/json');
            $response['success'] = true;
            echo json_encode($response);
        }
        else{
            #Failure
            $response['Codigo'] = 5;
            $response['Mensaje'] = "Fallo crear la ubicación";
        
            echo json_encode($response);
        }
    }

}
?>