<?php
require_once('config.php');

class administradorBD{
    
    //Función que ejecuta un query. Como parámetro se le introduce el string del query
	public function executeQuery($sql){
    
        //El programa se conecta al servidor de la base de datos
        $conecta = mysqli_connect(config::obtieneServidorBD(),config::obtieneUsuarioBD(),config::obtienePasswordBD());
        
        //En caso de error
		if(!$conecta){
			die('No puedo conectarme:' .mysqli_connect_error());
        }
        
        //El programa se conecta a la base de datos
		mysqli_select_db($conecta,config::obtieneNombreBD());
        
        //Se ejecuta el query y el resultado se guarda en un string
        $result = mysqli_query($conecta,$sql);
        
        //Se cierra el enlace con el servidor
        mysqli_close($conecta);
        
        //Se regresa el string del resultado    
		return $result;
	}
}

?>