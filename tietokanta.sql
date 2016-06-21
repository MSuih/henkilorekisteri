CREATE TABLE IF NOT EXISTS `mahdollisetroolit` (
  `rooliID` char(4) NOT NULL,
  `nimi` varchar(50) NOT NULL,
  `kuvaus` varchar(250) NOT NULL,
  PRIMARY KEY (`rooliID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 
 
INSERT INTO `mahdollisetroolit` (`rooliID`, `nimi`, `kuvaus`) VALUES
('tr01', 'Johtaja', 'Yhtiön johtaja joka tekee suurimamt päätökset ja istuu toimistoossa jokapäivä ja syö pullaa.'),
('tr02', 'Arkkitehti', 'Suunnittelee rakennukset ja seuraa niitten etenemistä'),
('tr03', 'Siivooja', 'Siivoaa'),
('tr04', 'Kirjanpitäjä', 'Laittaa palkat oikeisiin osotteisiin ja tutkii kuluja ja menoja'),
('tr05', 'Analysoija', 'toimii kirjanpitäjienkanssa ja suunnitelee miten yhtiö tekisi enemmän rahaa'),
('tr06', 'Rakentaja', 'normi työntekijä joka tekee vähän kaikkea'),
('tr07', 'Muurari', 'Leikkii laastilla koko päivän'),
('tr08', 'Hitsaaja', 'Hitsaa rautapaloja yhteen'),
('tr09', 'Ajaja', 'Ajaa vaikka traktoria'),
('tr10', 'Putkimies', 'Laittaa putkia sinne tänne'),
('tr11', 'Sähkömies', 'Asentaa sähköt rakennuksiin');
 
CREATE TABLE IF NOT EXISTS `työajat` (
  `työntekijäID` char(4) DEFAULT NULL,
  `ma_alku` time DEFAULT NULL,
  `ma_loppu` time DEFAULT NULL,
  `ti_alku` time DEFAULT NULL,
  `ti_loppu` time DEFAULT NULL,
  `ke_alku` time DEFAULT NULL,
  `ke_loppu` time DEFAULT NULL,
  `to_alku` time DEFAULT NULL,
  `to_loppu` time DEFAULT NULL,
  `pe_alku` time DEFAULT NULL,
  `pe_loppu` time DEFAULT NULL,
  `la_alku` time DEFAULT NULL,
  `la_loppu` time DEFAULT NULL,
  `su_alku` time DEFAULT NULL,
  `su_loppu` time DEFAULT NULL,
  KEY `työntekijäID` (`työntekijäID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 
INSERT INTO `työajat` (`työntekijäID`, `ma_alku`, `ma_loppu`, `ti_alku`, `ti_loppu`, `ke_alku`, `ke_loppu`, `to_alku`, `to_loppu`, `pe_alku`, `pe_loppu`, `la_alku`, `la_loppu`, `su_alku`, `su_loppu`) VALUES
('t044', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t041', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t024', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t004', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t045', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t035', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t005', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t026', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t025', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t043', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t042', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t023', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t001', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t007', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t006', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t031', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t034', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t033', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t032', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t003', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', NULL, NULL, NULL, NULL),
('t013', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', NULL, NULL, NULL, NULL),
('t014', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', NULL, NULL, NULL, NULL),
('t015', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', NULL, NULL, NULL, NULL),
('t016', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', NULL, NULL, NULL, NULL),
('t017', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', NULL, NULL, NULL, NULL),
('t018', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', '09:30:00', '16:30:00', NULL, NULL, NULL, NULL),
('t008', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', NULL, NULL, NULL, NULL),
('t027', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', NULL, NULL, NULL, NULL),
('t028', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', NULL, NULL, NULL, NULL),
('t040', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', NULL, NULL, NULL, NULL),
('t038', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', '09:00:00', '16:00:00', NULL, NULL, NULL, NULL),
('t000', '10:00:00', '17:00:00', '10:00:00', '17:00:00', '10:00:00', '17:00:00', '10:00:00', '17:00:00', '09:00:00', '16:00:00', NULL, NULL, NULL, NULL),
('t029', '09:00:00', '17:00:00', '10:00:00', '17:00:00', '10:00:00', '17:00:00', '10:00:00', '17:00:00', '09:00:00', '16:00:00', NULL, NULL, NULL, NULL),
('t012', '10:00:00', '17:00:00', '09:00:00', '17:00:00', '10:00:00', '17:00:00', '10:00:00', '17:00:00', '09:00:00', '16:00:00', NULL, NULL, NULL, NULL),
('t002', '09:00:00', '17:00:00', '10:00:00', '17:00:00', '10:00:00', '17:00:00', '10:00:00', '17:00:00', '09:00:00', '16:00:00', NULL, NULL, NULL, NULL),
('t009', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t010', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t011', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', '07:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t030', '09:00:00', '00:00:00', '09:00:00', '00:00:00', '09:00:00', '00:00:00', '09:00:00', '00:00:00', '09:00:00', '17:00:00', NULL, NULL, NULL, NULL),
('t039', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t022', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t039', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t019', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t020', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t021', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t037', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL),
('t036', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', '08:00:00', '15:00:00', NULL, NULL, NULL, NULL);
 
CREATE TABLE IF NOT EXISTS `työntekijä` (
  `työntekijäID` char(4) NOT NULL,
  `etunimi` varchar(50) NOT NULL,
  `sukunimi` varchar(50) NOT NULL,
  `osoite` varchar(50) NOT NULL,
  `puhelin` varchar(20) NOT NULL,
  `syntymäaika` date NOT NULL,
  `koulutus` varchar(50) NOT NULL,
  `aloitusaika` date NOT NULL,
  PRIMARY KEY (`työntekijäID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 
INSERT INTO `työntekijä` (`työntekijäID`, `etunimi`, `sukunimi`, `osoite`, `puhelin`, `syntymäaika`, `koulutus`, `aloitusaika`) VALUES
('t000', 'Samuel', 'Tunturinen', 'Mäkelentie 1 D 101', '0501280085', '1975-01-01', 'Tradenomi', '2014-11-01'),
('t001', 'Jukka', 'Jääpala', 'Jäätie 1 D 55', '0501234567', '1990-06-07', 'Hitsaaja', '2005-06-11'),
('t002', 'Jeremy', 'Dickinson', 'Hermokuja 1 A', '0503217654', '1970-11-22', 'Arkkitehti', '2005-06-11'),
('t003', 'Markus', 'Åhlberg', 'Satamasaarentie 1 D 49', '0506667777', '1996-12-06', 'Tradenomi', '2005-06-11'),
('t004', 'Tero', 'Nikkanen', 'Narrkkaritie 1 C 3', '0505567762', '1986-04-22', 'Muurari', '2013-05-13'),
('t005', 'Mikko', 'Lauttonen', 'Taikatie 5 A', '0501231234', '1988-12-06', 'Rakentaja', '2007-06-01'),
('t006', 'Chao ', 'Kurosaki ', 'Haajakatu 1 A 2', '04044368598 ', '1973-12-16', 'Putkimies', '1999-05-09'),
('t007', 'Mika ', 'Nikkonen ', 'Tervatie A', '05086577621 ', '1988-01-01', 'Hitsaaja', '2006-05-03'),
('t008', 'Arttu ', 'Männinen ', 'Muukalaiskuja A 5', '05098764521 ', '1995-10-28', 'Siivooja', '2006-04-12'),
('t009', 'Teemu ', 'Timmonen ', 'Kauppakartanonkatu B', '04086468468 ', '1990-06-18', 'työssäoppija', '2008-01-28'),
('t010', 'Lauri ', 'Ylen ', 'Virtakatu B 66', '0404144140 ', '1992-03-02', 'työssäoppija', '1999-07-28'),
('t011', 'Tatu ', 'Nyyman ', 'Fazerkuja C 2', '050 5195285 ', '1994-04-18', 'työssäoppija', '2000-07-28'),
('t012', 'Panu ', 'Virtanen ', 'Enjaksatätäenäätie 5', '086 4156841 ', '1980-03-18', 'Arkkitehti', '2002-04-29'),
('t013', 'Samu ', 'Kokkonen ', 'Jeesuksentaajama 5', '0509845865 ', '1981-06-18', 'Merkonomi', '2000-04-01'),
('t014', 'Jumbo ', 'Palomaa ', 'Vitunjärvi A 6', '0504641646 ', '1988-05-09', 'Tradenomi', '1996-12-24'),
('t015', 'Alex', 'Milani', 'Herpdeprroad 1', '05055686482', '1986-12-24', 'Merkonomi', '2000-01-01'),
('t016', 'Aaro', 'Hietanen', 'Pälkkykuja  A 1', '0503214915', '1985-05-06', 'Merkonomi', '2008-04-04'),
('t017', 'Eetu', 'Kaskijoki', 'Puutarhakatu 1 A', '0409483951', '1983-11-12', 'Tradenomi', '2009-04-04'),
('t018', 'Elisa', 'Helen', 'Rauhankatu 5', '0503956810', '1990-02-03', 'Merkonomi', '2007-09-10'),
('t019', 'Herbertti', 'Kaikunen', 'Koulukatu', '050285622', '1987-01-01', 'Tradenomi', '2001-01-02'),
('t020', 'Herbertti', 'Kaikunen', 'Koulukatu', '050285622', '1987-01-01', 'Tradenomi', '2001-01-02'),
('t021', 'Jaana', 'Venenen', 'Hauta-aho', '050285622', '1987-01-01', 'Merkonomi', '2001-01-02'),
('t022', 'Jere', 'Kaikunen', 'Koulukatu', '050285622', '1987-01-01', 'Muurari', '2001-01-02'),
('t023', 'Jukka', 'Lindman', 'Linnankatu 6', '0507247777', '1990-10-09', 'Hitsaaja', '2008-02-05'),
('t024', 'Jyri', 'Sulander', 'Brahenkatu 4 B 4', '050667842', '1988-10-10', 'Muurari', '2007-02-02'),
('t025', 'Kia', 'Berglund', 'Maariankatu 3 C', '0509751357', '1979-02-02', 'Rakentaja', '2004-08-11'),
('t026', 'Kimi', 'Eskelinen', 'Pitkäkatu C', '050192837', '1982-05-11', 'Rakentaja', '2005-09-09'),
('t027', 'Markku', 'Kytönen', 'Suutarinkatu 3 A', '040849205', '1990-09-10', 'Siivooja', '2009-02-07'),
('t028', 'Markus ', 'Numminen', 'Raunintie 5 ', '0401847766', '1987-08-12', 'Siivooja', '2002-09-12'),
('t029', 'Martta', 'Haatainen', 'Murtomaantie 5 B', '0401488424', '1975-11-12', 'Arkkitehti', '2000-01-01'),
('t030', 'Mika', 'Pöyry', 'Allinkatu 5', '0404489582', '1984-01-05', 'Insinööri', '2000-01-01'),
('t031', 'Pertti', 'Kirvesmies', 'Lokinkatu 4', '0501335687', '1990-02-05', 'Putkimies', '2002-08-03'),
('t032', 'Roope', 'Palander', 'Virusmäentie A 1', '0504666578', '2007-01-01', 'Sähkömies', '2004-03-06'),
('t033', 'Sanna', 'Polvi', 'Puistotie 1', '0503555687', '1986-06-03', 'Sähkömies', '2002-02-02'),
('t034', 'Teemu', 'Utrio', 'Urheilutie 5 B', '0504662598', '1983-09-12', 'Sähkömies', '2007-03-10'),
('t035', 'Teppi', 'Vehviläinen', 'Emponkaari A', '0505775687', '1981-08-09', 'Rakentaja', '2005-03-06'),
('t036', 'Minna', 'Sistonen', 'Akomäentie 88', '0503214915', '1989-02-10', 'Merkonomi', '2002-02-05'),
('t037', 'Aliisa', 'Timonen', 'Maaningantie 22', '0409483951', '1988-10-11', 'Tradenomi', '2005-01-10'),
('t038', 'Jarkko', 'Mustonen', 'Torikatu 47', '0503956810', '1985-10-11', 'Merkonomi', '2010-11-11'),
('t039', 'Teppo', 'Otakaari', 'Hätilänkatu 97', '0502856202', '1980-05-06', 'Tradenomi', '2004-09-08'),
('t040', 'Jussi', 'Rislakki', 'Suometsäntie 40', '0508239512', '1990-12-10', 'Merkonomi', '2008-02-11'),
('t041', 'Jouni', 'Päätalo', 'Sahantie 91', '0409876543', '1975-05-11', 'Muurari', '2001-02-04'),
('t042', 'Matilda', 'Saijomaa', 'Massbyntie 1', '0500192837', '1986-11-11', 'Hitsaaja', '2002-05-03'),
('t043', 'Jonne', 'Sistonen', 'Visiokatu 2', '0507247777', '1979-02-11', 'Hitsaaja', '2001-02-05'),
('t044', 'Tommi', 'Anttila', 'Säkkiläntie 6', '0506678423', '1990-05-02', 'Muurari', '2015-01-01'),
('t045', 'Kaarle', 'Lehtämäki', 'Akonmäentie 22', '0509751357', '1990-01-01', 'Rakentaja', '2015-01-01');
 
CREATE TABLE IF NOT EXISTS `työpaikka` (
  `työpaikkaID` char(4) NOT NULL,
  `nimi` varchar(50) NOT NULL,
  `osoite` varchar(50) NOT NULL,
  `aukioloajat` varchar(50) NOT NULL,
  `arvioituvalmistumisaika` varchar(6) NOT NULL,
  PRIMARY KEY (`työpaikkaID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 
INSERT INTO `työpaikka` (`työpaikkaID`, `nimi`, `osoite`, `aukioloajat`, `arvioituvalmistumisaika`) VALUES
('ty01', 'Helsingin Pääkonttori', 'Abrahaminkatu 2', '09:00-17:00', 'VALMIS'),
('ty02', 'Pasilan Urheiluhalli', 'Ratapihantie ', '08:00-17:00', '2020'),
('ty03', 'Herttoniemen Bussikeskus', 'Jokutie 5', '08:00-17:00', '2016');
 
CREATE TABLE IF NOT EXISTS `työrooli` (
  `työrooliID` smallint UNSIGNED NOT NULL AUTO_INCREMENT,
  `työntekijäID` char(4) NOT NULL,
  `työpaikkaID` char(4) NOT NULL,
  `rooliID` char(4) NOT NULL,
  `kuvaus` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`työrooliID`),
  KEY `työntekijäID` (`työntekijäID`),
  KEY `työpaikkaID` (`työpaikkaID`),
  KEY `rooliID` (`rooliID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 
 INSERT INTO `työrooli` (`työntekijäID`, `työpaikkaID`, `rooliID`, `kuvaus`) VALUES
('t030', 'ty01', 'tr01', 'Johtaa koko yhtiötä'),
('t012', 'ty01', 'tr02', 'Suunnittelee rakennuksia'),
('t002', 'ty01', 'tr02', 'Suunnittelee rakennuksia'),
('t008', 'ty01', 'tr03', 'Siivoilee konttoreita'),
('t003', 'ty01', 'tr04', 'Pitää kirjaa tuloista ja menoista'),
('t013', 'ty01', 'tr05', 'Analysoi tilastoja ja rahoja'),
('t001', 'ty02', 'tr08', 'Hitsaa juttuja yhteen'),
('t004', 'ty03', 'tr07', 'Muuraa taloja'),
('t007', 'ty02', 'tr08', 'Hitsaa juttuja yhteen'),
('t025', 'ty03', 'tr09', 'Ajelee rekkoja'),
('t006', 'ty02', 'tr10', 'Asentelee putkia'),
('t032', 'ty03', 'tr11', 'Asentelee sähköjä'),
('t036', 'ty03', 'tr11', 'Asentelee sähköjä'),
('t029', 'ty01', 'tr02', 'Suunnittelee rakennuksia'),
('t027', 'ty01', 'tr03', 'Siivoilee konttoreita'),
('t028', 'ty01', 'tr03', 'Siivoilee konttoreita'),
('t000', 'ty01', 'tr04', 'Pitää kirjaa tuloista ja menoista'),
('t014', 'ty01', 'tr04', 'Pitää kirjaa tuloista ja menoista'),
('t017', 'ty01', 'tr04', 'Pitää kirjaa tuloista ja menoista'),
('t019', 'ty01', 'tr04', 'Pitää kirjaa tuloista ja menoista'),
('t037', 'ty01', 'tr04', 'Pitää kirjaa tuloista ja menoista'),
('t039', 'ty01', 'tr04', 'Pitää kirjaa tuloista ja menoista'),
('t020', 'ty01', 'tr04', 'Pitää kirjaa tuloista ja menoista'),
('t015', 'ty01', 'tr05', 'Analysoi tilastoja ja rahoja'),
('t016', 'ty01', 'tr05', 'Analysoi tilastoja ja rahoja'),
('t018', 'ty01', 'tr05', 'Analysoi tilastoja ja rahoja'),
('t021', 'ty01', 'tr05', 'Analysoi tilastoja ja rahoja'),
('t034', 'ty03', 'tr11', 'Asentelee sähköjä'),
('t005', 'ty02', 'tr06', 'rakentelee taloja'),
('t009', 'ty02', 'tr09', 'Ajelee rekkoja'),
('t010', 'ty03', 'tr07', 'Muuraa taloja'),
('t011', 'ty02', 'tr10', 'Asentelee putkia'),
('t023', 'ty03', 'tr08', 'Hitsaa juttuja yhteen'),
('t042', 'ty03', 'tr08', 'Hitsaa juttuja yhteen'),
('t043', 'ty03', 'tr08', 'Hitsaa juttuja yhteen'),
('t038', 'ty01', 'tr05', 'Analysoi tilastoja ja rahoja'),
('t040', 'ty01', 'tr05', 'Analysoi tilastoja ja rahoja'),
('t031', 'ty03', 'tr10', 'Asentelee putkia'),
('t026', 'ty03', 'tr06', 'rakentelee taloja'),
('t035', 'ty03', 'tr06', 'rakentelee taloja'),
('t045', 'ty02', 'tr06', 'rakentelee taloja'),
('t022', 'ty02', 'tr07', 'Muuraa taloja'),
('t041', 'ty02', 'tr07', 'Muuraa taloja'),
('t024', 'ty02', 'tr07', 'Muuraa taloja'),
('t044', 'ty03', 'tr07', 'Muuraa taloja'),
('t033', 'ty02', 'tr11', 'Asentelee sähköjä');
 
CREATE TABLE IF NOT EXISTS `työväline` (
  `työvälineID` char(5) NOT NULL,
  `Nimi` varchar(50) NOT NULL,
  `valmistaja` varchar(50) NOT NULL,
  `myyjä` varchar(50) NOT NULL,
  PRIMARY KEY (`työvälineID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 
INSERT INTO `työväline` (`työvälineID`, `Nimi`, `valmistaja`, `myyjä`) VALUES
('tv001', 'akkuporakone', 'bosch', 'k-rauta'),
('tv002', 'vasara', 'fiskars', 'k-rauta'),
('tv003', 'ruuvimeisseli', 'bahco', 'k-rauta'),
('tv004', 'mitta', 'kapro', 'motonet'),
('tv005', 'sirkkeli', 'bacho', 'k-rauta'),
('tv006', 'hiomakone', 'bosch', 'motonet'),
('tv007', 'hitsauskone', 'bosch', 'motonet'),
('tv008', 'pihdit', 'fiskars', 'motonet'),
('tv009', 'sorkkarauta', 'fiskars', 'k-rauta'),
('tv010', 'vatupassi', 'kapro', 'k-rauta'),
('tv011', 'kirves', 'fiskars', 'motonet'),
('tv012', 'leka', 'xnipex', 'bauhaus'),
('tv013', 'voimapihdit', 'knipex', 'k-rauta'),
('tv014', 'sivuleikkurit', 'knpex', 'biltema'),
('tv015', 'jakoavain', 'bacho', 'biltema'),
('tv016', 'lapio', 'fiskars', 'bauhaus'),
('tv017', 'kottikärryt', 'fiskars', 'bauhaus'),
('tv018', 'naulapyssy', 'fiskars', 'bauhaus'),
('tv019', 'saha', 'fiskars', 'bauhaus'),
('tv020', 'taltta', 'bacho', 'k-rauta');
 
CREATE TABLE IF NOT EXISTS `varaus` (
  `varausID` char(5) NOT NULL,
  `työvälineID` char(5) NOT NULL,
  `työntekijäID` char(4) NOT NULL,
  `varaus_Alku` date NOT NULL,
  `varaus_Loppu` date NOT NULL,
  PRIMARY KEY (`varausID`),
  KEY `työväline_ibfk_1` (`työvälineID`),
  KEY `työntekijä_ibfk_1` (`työntekijäID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 
ALTER TABLE `työajat`
  ADD CONSTRAINT `työajat_ibfk_1` FOREIGN KEY (`työntekijäID`) REFERENCES `työntekijä` (`työntekijäID`) ON DELETE NO ACTION ON UPDATE CASCADE;
 
ALTER TABLE `työrooli`
  ADD CONSTRAINT `työrooli_ibfk_1` FOREIGN KEY (`työntekijäID`) REFERENCES `työntekijä` (`työntekijäID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `työrooli_ibfk_2` FOREIGN KEY (`työpaikkaID`) REFERENCES `työpaikka` (`työpaikkaID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `työrooli_ibfk_3` FOREIGN KEY (`rooliID`) REFERENCES `mahdollisetroolit` (`rooliID`) ON DELETE NO ACTION ON UPDATE CASCADE;
 
ALTER TABLE `varaus`
  ADD CONSTRAINT `työntekijä_ibfk_1` FOREIGN KEY (`työntekijäID`) REFERENCES `työntekijä` (`työntekijäID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `työväline_ibfk_1` FOREIGN KEY (`työvälineID`) REFERENCES `työväline` (`työvälineID`) ON DELETE NO ACTION ON UPDATE CASCADE;