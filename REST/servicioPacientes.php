<?php

    include('config.php');
    include('administradorBD.php');

    $db = new administradorBD();

    if($_SERVER['REQUEST_METHOD']=='GET'){

        $query = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno, Matricula FROM Pacientes";

        $result = $db->executeQuery($query);

        $pacientes = array();

        if(mysqli_num_rows($result)) {
            while($paciente = mysqli_fetch_assoc($result)) {
                    
                    #Agregar Padecimientos al paciente
                    $query2 = "SELECT Padecimientos FROM HistorialMedico WHERE Pacientes_Matricula = '".$paciente['Matricula']."'";
                    $result2 = $db->executeQuery($query2);

                    if(mysqli_num_rows($result2)) {
                        while($padecimientos = mysqli_fetch_assoc($result2)){
                            $paciente['Padecimientos'] = $padecimientos['Padecimientos'];
                        }
                    }

                    #Agregar Medicamentos al paciente
                    $query3 = "SELECT Medicamentos FROM HistorialMedico WHERE Pacientes_Matricula = '".$paciente['Matricula']."'";
                    $result3 = $db->executeQuery($query3);

                    if(mysqli_num_rows($result3)) {
                        while($Medicamentos = mysqli_fetch_assoc($result3)){
                            $paciente['Medicamentos'] = $Medicamentos['Medicamentos'];
                        }
                    }

                    $pacientes[] = $paciente;
            }
        }

        header('Content-type: application/json');
        echo json_encode($pacientes);

    }
?>
