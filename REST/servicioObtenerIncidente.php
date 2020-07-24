<?php

    include('config.php');
    include('administradorBD.php');

    $db = new administradorBD();

    if($_SERVER['REQUEST_METHOD']=='GET'){

        $query = "SELECT Pacientes_Matricula, Descripcion FROM Incidentes";

        $result = $db->executeQuery($query);

        $incidentes = array();

        if(mysqli_num_rows($result)) {
            while($incidente = mysqli_fetch_assoc($result)) {
                    
                    #Agregar Padecimientos al paciente
                    $query2 = "SELECT Nombre, ApellidoPaterno, NombreContactoEmergencia, NumContactoEmergencia FROM Pacientes WHERE Matricula = '".$incidente['Pacientes_Matricula']."'";
                    $result2 = $db->executeQuery($query2);

                    if(mysqli_num_rows($result2)) {
                        while($paciente = mysqli_fetch_assoc($result2)){
                            $incidente['Nombre'] = $paciente['Nombre'];
                            $incidente['ApellidoPaterno'] = $paciente['ApellidoPaterno'];
                            $incidente['NombreContactoEmergencia'] = $paciente['NombreContactoEmergencia'];
                            $incidente['NumContactoEmergencia'] = $paciente['NumContactoEmergencia'];
                        }
                    }

                    $incidentes[] = $incidente;
            }
        }

        header('Content-type: application/json');
        echo json_encode($incidentes);

    }
?>
