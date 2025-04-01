-- Aquest trigger s’executa automàticament després d’eliminar una col·lecció.
-- El seu objectiu és eliminar totes les relacions d’usuaris que seguien la col·lecció eliminada
CREATE TRIGGER TR_DeleteCollectionFollowings
ON [Collection]
AFTER DELETE
AS
BEGIN
    DELETE FROM User_Follow_Collection
    WHERE collection_id IN (SELECT collection_id FROM DELETED);
END;