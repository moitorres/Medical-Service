

-- -----------------------------------------------------
-- Table Pacientes
-- -----------------------------------------------------
CREATE TABLE Pacientes(
  Matricula VARCHAR(12) NOT NULL,
  Nombre VARCHAR(25) NOT NULL,
  ApellidoPaterno VARCHAR(25) NOT NULL,
  ApellidoMaterno VARCHAR(25) NOT NULL,
  Edad INT NOT NULL,
  NombreContactoEmergencia VARCHAR(100) NOT NULL,
  NumContactoEmergencia INT NOT NULL,
  PRIMARY KEY (Matricula));



-- -----------------------------------------------------
-- Table Doctores
-- -----------------------------------------------------
CREATE TABLE Doctores (
  Usuario VARCHAR(100) NOT NULL,
  Nombre VARCHAR(25) NOT NULL,
  ApellidoPaterno VARCHAR(25) NOT NULL,
  ApellidoMaterno VARCHAR(25) NOT NULL,
  PRIMARY KEY (Usuario));



-- -----------------------------------------------------
-- Table Login
-- -----------------------------------------------------
CREATE TABLE Login (
  idLogin INT AUTO_INCREMENT,
  Pacientes_Matricula VARCHAR(12) NULL,
  Doctores_Usuario VARCHAR(100) NULL,
  Password VARCHAR(25) NOT NULL,
  PRIMARY KEY (idLogin),
  FOREIGN KEY (Pacientes_Matricula) REFERENCES Pacientes (Matricula),
  FOREIGN KEY (Doctores_Usuario) REFERENCES Doctores (Usuario));



-- -----------------------------------------------------
-- Table Ubicaciones
-- -----------------------------------------------------
CREATE TABLE Ubicaciones (
  idUbicaciones INT AUTO_INCREMENT,
  Pacientes_Matricula VARCHAR(12) NOT NULL,
  Longitud VARCHAR(45) NOT NULL,
  Latitud VARCHAR(45) NOT NULL,
  PRIMARY KEY (idUbicaciones),
  FOREIGN KEY (Pacientes_Matricula) REFERENCES Pacientes (Matricula));

-- -----------------------------------------------------
-- Table Incidentes
-- -----------------------------------------------------
CREATE TABLE Incidentes (
  idIncidentes INT AUTO_INCREMENT,
  Pacientes_Matricula VARCHAR(12) NOT NULL,
  Doctores_Usuario VARCHAR(100) NULL,
  Ubicaciones_id INT NULL,
  NotaDeVoz VARCHAR(100) NULL,
  PRIMARY KEY (idIncidentes),
  FOREIGN KEY (Pacientes_Matricula) REFERENCES Pacientes (Matricula),
  FOREIGN KEY (Doctores_Usuario) REFERENCES Doctores (Usuario),
  FOREIGN KEY (Ubicaciones_id) REFERENCES Ubicaciones (idUbicaciones));




-- -----------------------------------------------------
-- Table HistorialMedico
-- -----------------------------------------------------
CREATE TABLE HistorialMedico (
  idHistorialMedico INT NOT NULL AUTO_INCREMENT,
  Pacientes_Matricula VARCHAR(12) NOT NULL,
  Padecimientos VARCHAR(500) NULL,
  Medicamentos VARCHAR(500) NULL,
  Incidentes_Id INT NULL,
  PRIMARY KEY (idHistorialMedico),
  FOREIGN KEY (Incidentes_Id) REFERENCES Incidentes (idIncidentes),
  FOREIGN KEY (Pacientes_Matricula) REFERENCES Pacientes (Matricula));
