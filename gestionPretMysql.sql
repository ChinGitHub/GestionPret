
CREATE TABLE Client(
	id int NOT NULL AUTO_INCREMENT,
	idTypeClient int NOT NULL,
	val varchar(50) NULL,
 CONSTRAINT PK_Client PRIMARY KEY  
(
	id 
));
CREATE TABLE TypeClient(
	id int NOT NULL AUTO_INCREMENT,
	val varchar(50) NULL,
 CONSTRAINT PK_TypeClient PRIMARY KEY  
(
	id 
));
CREATE TABLE DemandePret(
	id int NOT NULL AUTO_INCREMENT,
	idClient int NOT NULL,
	dateDemande date NOT NULL,
	montant float NULL,
	interet real NULL,
	duree int NOT NULL,
	valide boolean NOT NULL,
	dateValidation date NULL,
 CONSTRAINT PK_DemandePret PRIMARY KEY  
(
	id 
));
CREATE VIEW DemandePretVueLib AS SELECT        DemandePret.id AS id, TypeClient.id AS idTypeClient, Client.id AS idClient, TypeClient.val AS typeClient, Client.val AS client, DemandePret.dateDemande,DemandePret.dateValidation, 
                         DemandePret.montant, DemandePret.interet, DemandePret.duree, DemandePret.valide, month(DemandePret.dateDemande) AS mois, year(DemandePret.dateDemande) AS annee
FROM            DemandePret INNER JOIN
                         Client ON Client.id = DemandePret.idClient INNER JOIN
                         TypeClient ON Client.idTypeClient = TypeClient.id;

CREATE TABLE RemboursementReel(
	id int NOT NULL AUTO_INCREMENT,
	DateRemboursement date NOT NULL,
	ResteCapital float NOT NULL,
	echeance float NOT NULL,
	interet float NOT NULL,
	idClient int NULL,
	idDemandePret int NULL,
	idRemboursementPrev int NULL,
	isPenalite bit NULL,
 CONSTRAINT PK_RemboursementReel PRIMARY KEY  
(
	id 
));
CREATE VIEW RemboursementReelLib
AS
SELECT        RemboursementReel.id,RemboursementReel.DateRemboursement, RemboursementReel.ResteCapital, RemboursementReel.echeance, RemboursementReel.interet, 
                         RemboursementReel.idClient, Client.val AS client, Client.idTypeClient, TypeClient.val AS typeClient, RemboursementReel.idDemandePret, 
                         DemandePret.dateDemande AS dateDemandePret, DemandePret.montant, DemandePret.interet AS tauxInteret, DemandePret.duree, DemandePret.valide,DemandePret.dateValidation, 
						month(RemboursementReel.DateRemboursement) AS mois, year(RemboursementReel.DateRemboursement) AS annee
FROM            RemboursementReel INNER JOIN
                         DemandePret ON RemboursementReel.idDemandePret = DemandePret.id INNER JOIN
                         Client ON RemboursementReel.idClient = Client.id INNER JOIN
                         TypeClient ON Client.idTypeClient = TypeClient.id;

CREATE TABLE RemboursementPrevisionnel(
	id int NOT NULL AUTO_INCREMENT,
	DateRemboursement date NOT NULL,
	ResteCapital float NOT NULL,
	echeance float NOT NULL,
	interet float NOT NULL,
	idClient int NULL,
	idDemandePret int NULL,
	isPenalite bit NULL,
	isPaye bit NULL,
 CONSTRAINT PK_RemboursementPrevisionnel PRIMARY KEY  
(
	id 
));
CREATE VIEW PenalisationParMoisAn AS SELECT RemboursementPrevisionnel.*,month(RemboursementPrevisionnel.DateRemboursement) AS mois, year(RemboursementPrevisionnel.DateRemboursement) AS annee FROM RemboursementPrevisionnel LEFT JOIN RemboursementReel ON RemboursementPrevisionnel.id=RemboursementReel.idRemboursementPrev WHERE RemboursementReel.idRemboursementPrev IS NULL;

CREATE VIEW PenalisationParMoisAnLib AS SELECT PenalisationParMoisAn.*, DemandePretVueLib.idTypeClient,DemandePretVueLib.typeClient,DemandePretVueLib.client,DemandePretVueLib.dateValidation,DemandePretVueLib.montant,DemandePretVueLib.interet AS tauxInteret,DemandePretVueLib.duree FROM PenalisationParMoisAn JOIN DemandePretVueLib ON DemandePretVueLib.id=PenalisationParMoisAn.idDemandePret;

CREATE VIEW ClientLib AS SELECT Client.*, TypeClient.val as typeClient from Client JOIN TypeClient ON Client.idTypeClient=TypeClient.id;

CREATE VIEW RemboursementPrevLib
AS
SELECT        RemboursementPrevisionnel.id, RemboursementPrevisionnel.DateRemboursement, RemboursementPrevisionnel.ResteCapital, RemboursementPrevisionnel.echeance, RemboursementPrevisionnel.interet, 
                         RemboursementPrevisionnel.idClient, Client.val AS client, Client.idTypeClient, TypeClient.val AS typeClient, RemboursementPrevisionnel.idDemandePret,RemboursementPrevisionnel.isPenalite,RemboursementPrevisionnel.isPaye,
                         DemandePret.dateDemande AS dateDemandePret, DemandePret.montant, DemandePret.interet AS tauxInteret, DemandePret.duree, DemandePret.valide, DemandePret.dateValidation, 
                         month(RemboursementPrevisionnel.DateRemboursement) AS moiss,year(RemboursementPrevisionnel.DateRemboursement) AS annee
FROM            RemboursementPrevisionnel INNER JOIN
                         DemandePret ON RemboursementPrevisionnel.idDemandePret = DemandePret.id INNER JOIN
                         Client ON RemboursementPrevisionnel.idClient = Client.id INNER JOIN
                         TypeClient ON Client.idTypeClient = TypeClient.id;

CREATE TABLE Action(
	id int NOT NULL AUTO_INCREMENT,
	action varchar(50) NULL,
	nomTable varchar(50) NULL,
	idRole int NULL
);

CREATE TABLE Fonctionnement(
	id int NOT NULL AUTO_INCREMENT,
	mois int NOT NULL,
	annee int NOT NULL,
	montant float NOT NULL,
 CONSTRAINT PK_Fonctionnement PRIMARY KEY  
(
	id 
));
CREATE TABLE JourFerie(
	id int NOT NULL AUTO_INCREMENT,
	jour date NOT NULL,
	val varchar(50) NOT NULL,
	CONSTRAINT PK_JourFerie PRIMARY KEY  
(
	id 
));
CREATE TABLE PlanRemboursement(
	id int NOT NULL AUTO_INCREMENT,
	idClient int NULL,
	idDemandePret int NULL,
	DateRemboursement date NULL,
	ResteCapital float NULL,
	echeance float NULL,
	interet float NULL,
    CONSTRAINT PK_PlanRemboursement PRIMARY KEY  
(
	id 
));
CREATE TABLE Report(
	id int NOT NULL AUTO_INCREMENT,
	annee int NOT NULL,
	montant float NOT NULL,
 CONSTRAINT PK_Report PRIMARY KEY  
(
	id 
));

CREATE TABLE Utilisateur(
	id int NOT NULL AUTO_INCREMENT,
	login varchar(50) NOT NULL,
	password varchar(50) NOT NULL,
	idrole int NULL,
	idTypeUtilisateur int NULL,
     CONSTRAINT PK_Utilisateur PRIMARY KEY  
(
	id 
)
);

