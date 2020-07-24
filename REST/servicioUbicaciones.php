<?php

    include('config.php');
    include('administradorBD.php');

    $db = new administradorBD();

    if($_SERVER['REQUEST_METHOD']=='GET'){

        $query = "SELECT Pacientes_Matricula, Longitud, Latitud FROM Ubicaciones";

        $result = $db->executeQuery($query);

        $ubicaciones = array();

        if(mysqli_num_rows($result)) {
            while($ubicacion = mysqli_fetch_assoc($result)) {
                    
                    #search for the patient
                    $query2 = "SELECT Nombre, ApellidoPaterno FROM Pacientes WHERE Matricula = '".$ubicacion['Pacientes_Matricula']."'";
                    $result2 = $db->executeQuery($query2);

                    #Add name and last name of the patient
                    if(mysqli_num_rows($result2)) {
                        while($paciente = mysqli_fetch_assoc($result2)){
                            $ubicacion['Nombre'] = $paciente['Nombre'];
                            $ubicacion['ApellidoPaterno'] = $paciente['ApellidoPaterno'];
                        }
                    }

                    $ubicaciones[] = $ubicacion;
            }
        }

        header('Content-type: application/json');
        echo json_encode($ubicaciones);

    }
?>
