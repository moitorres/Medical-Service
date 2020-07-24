<?php

require_once('pacientes.php');

$pacientes = new pacientes();

echo $pacientes->obtenerJSONPacientes();

?>