-- Aquest trigger s�executa autom�ticament despr�s d�eliminar una col�lecci�.
-- El seu objectiu �s eliminar totes les relacions d�usuaris que seguien la col�lecci� eliminada
CREATE TRIGGER TR_DeleteCollectionFollowings
ON [Collection]
AFTER DELETE
AS
BEGIN
    DELETE FROM User_Follow_Collection
    WHERE collection_id IN (SELECT collection_id FROM DELETED);
END;