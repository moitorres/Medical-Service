<?php
# incorporamos el archivo config.php
include('config.php');
include('administradorBD.php');

$db = new administradorBD();

if($_SERVER['REQUEST_METHOD']=='POST'){
  
    #Get all the variables inserted
    $Matricula = $_POST['Matricula'];
    $Descripcion = $_POST['Descripcion'];

    #Query for searching for the last location of the patient
    $query1 = "SELECT IdUbicaciones FROM Ubicaciones WHERE Pacientes_Matricula = '$Matricula'";

    #Execute the first query
    $result = $db->executeQuery($query1);

    #If the location of the user is found
    if(mysqli_num_rows($result)){
        while($ubicacion = mysqli_fetch_assoc($result)) {

            $query2="INSERT INTO Incidentes(Pacientes_Matricula, Ubicaciones_Id, Descripcion) VALUES ('$Matricula', ".$ubicacion['IdUbicaciones'].",'$Descripcion')";
        
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
                $response['Mensaje'] = "Falló mandar notificación";
            
                echo json_encode($response);
            }
        }
    }
    #If no location is found
    else{

        $query2="INSERT INTO Incidentes(Pacientes_Matricula, Descripcion) VALUES ('$Matricula','$Descripcion')";
        
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
            $response['Mensaje'] = "Falló mandar notificación";
        
            echo json_encode($response);
        }
        
    }

}
?>