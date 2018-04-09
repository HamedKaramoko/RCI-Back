--
-- Base de données :  `RCI_DB`
--

-- --------------------------------------------------------

--
-- Structure de la table `Person`
--

CREATE TABLE `Person` (
  `id` int(11) NOT NULL PRIMARY KEY,
  `login` varchar(20) DEFAULT NULL UNIQUE,
  `password` varchar(20) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `email` varchar(40) DEFAULT NULL UNIQUE
);

-- --------------------------------------------------------

--
-- Structure de la table `Service`
--

CREATE TABLE `Service` (
  `id` int(11) NOT NULL PRIMARY KEY,
  `label` varchar(50) NOT NULL UNIQUE,
  `description` varchar(200) NOT NULL,
  `cost` double NOT NULL
);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `Person`
--
ALTER TABLE `Person`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `Service`
--
ALTER TABLE `Service`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
