<?php
# incorporamos el archivo config.php
include('config.php');
include('administradorBD.php');

$db = new administradorBD();

if($_SERVER['REQUEST_METHOD']=='GET'){
  
  $usuario = $_GET['usuario'];
  $password = $_GET['password'];
  
  #Queries for the users
  $query = "SELECT idLogin, Pacientes_Matricula, Password FROM Login WHERE Pacientes_Matricula = '$usuario' AND Password='$password'";
  $query2 = "SELECT Matricula, Nombre, ApellidoPaterno, ApellidoMaterno, Edad FROM Pacientes WHERE Matricula = '$usuario';";

  #Queries for the doctors
  $query3 = "SELECT idLogin, Doctores_Usuario, Password FROM Login WHERE Doctores_Usuario = '$usuario' AND Password='$password'";
  $query4 = "SELECT Usuario, Nombre, ApellidoPaterno, ApellidoMaterno FROM Doctores WHERE Usuario = '$usuario';";

  #Execute the queries of the users
  $res1 = $db->executeQuery($query);
  $res2 = $db->executeQuery($query2);

  #Execute the queries of the doctors
  $res3 = $db->executeQuery($query3);
  $res4 = $db->executeQuery($query4);

  $login = array();
  $usuarios = array();
  
  #If someone with the correct username and password is found
  if(mysqli_num_rows($res1)){

    #information from the first query
    while($username = mysqli_fetch_assoc($res1)) {
      $login[] = $username;
    }  	  

    #information from the second query
    if(mysqli_num_rows($res2)) {
      while($usuario = mysqli_fetch_assoc($res2)) {
        $usuarios[] = $usuario;
      }
    }
    
    #success code
    $codigo[] = array('Codigo' => '01', 'Mensaje' => 'Bienvenido');	 

    #add the three arrays to the result
    $result = array_merge($codigo,$login,$usuarios);
  
    header('Content-type: application/json');
    echo json_encode($result);
  }

  #If a doctor with the correct username and password is found
  else if(mysqli_num_rows($res3)){

    #information from the first query
    while($username = mysqli_fetch_assoc($res3)) {
      $login[] = $username;
    }  	  

    #information from the second query
    if(mysqli_num_rows($res4)) {
      while($usuario = mysqli_fetch_assoc($res4)) {
        $usuarios[] = $usuario;
      }
    }
    
    #success code
    $codigo[] = array('Codigo' => '02', 'Mensaje' => 'Bienvenido Doctor');	 

    #add the three arrays to the result
    $result = array_merge($codigo,$login,$usuarios);
  
    header('Content-type: application/json');
    echo json_encode($result);
  }

  #if the user or password are incorrect
  else{
	 $codigo[] = array('Codigo' => '04', 'Mensaje' => 'Usuario o password incorrecto');	
     echo json_encode($codigo);
  }
}
?>