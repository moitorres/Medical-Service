<?php
require_once('config.php');
require_once('administradorBD.php');

class pacientes{
	
	private function obtenerPacientes(){
		
        //Query to get all the patients
        $sql = "SELECT * FROM Pacientes";
	
        //An administradorBD object is created
        $db = new administradorBD();

        //The function executeQuery is used to get the result of the query
		return $db->executeQuery($sql);
    }

	public function obtenerJSONPacientes(){
		$json="";
		$i=0;

		$result = $this->obtenerPacientes(); #Query for getting the patients
		

		$json .= "{\"Pacientes\" : [ ";
		
		while($row = mysqli_fetch_array($result, MYSQLI_BOTH)){

			if($i >0)
				$json .= ",";
				$json .= "{ \"Matricula\": ".$row['Matricula'].", \"Nombre\":\"".$row['Nombre']."\", \"AppelidoPaterno\":\"".$row['ApellidoPaterno']."\", \"AppelidoMaterno\":\"".$row['ApellidoMaterno']."\", \"Edad\": \"".$row['Edad']."\", ";
				$json .= "\"NombreContactoEmergencia\":\"".$row['NombreContactoEmergencia']."\", \"NumContactoEmergencia\":\"".$row['NumContactoEmergencia']."\"";
				$json .= "}";
				$i++;		
		}

		$json .= "]";
		$json .= "}";	
		
		return $json;
	}

	public function obtenerHistorialMedico($Matricula){
		
        //Query for getting the medical record of a patient
        $sql = "SELECT Padecimientos, Medicamentos FROM HistorialMedico WHERE Pacientes_Matricula = '$Matricula'";
	
        $db = new administradorBD();
		return $db->executeQuery($sql);
	}	

}

?>