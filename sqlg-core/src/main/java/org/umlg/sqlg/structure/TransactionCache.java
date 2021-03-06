package org.umlg.sqlg.structure;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A transaction scoped cache.
 * Date: 2014/10/04
 * Time: 3:20 PM
 */
public class TransactionCache {

    private Connection connection;
    private Map<ElementPropertyRollback, Object> elementPropertyRollbackFunctions = new WeakHashMap<>();
    private BatchManager batchManager;
    private Map<RecordId, SqlgVertex> vertexCache = new WeakHashMap<>();
    //true if a schema modification statement has been executed.
    //it is important to know this as schema modification creates exclusive locks.
    //In particular it locks querying the schema itself.
    private boolean schemaModification = false;

    static TransactionCache of(Connection connection, BatchManager batchManager) {
        return new TransactionCache(connection, batchManager);
    }

    private TransactionCache(
            Connection connection,
            BatchManager batchManager) {

        this.connection = connection;
        this.batchManager = batchManager;
    }

    Connection getConnection() {
        return this.connection;
    }

    Map<ElementPropertyRollback, Object> getElementPropertyRollback() {
        return this.elementPropertyRollbackFunctions;
    }

    BatchManager getBatchManager() {
        return this.batchManager;
    }

    void clear() {
        this.elementPropertyRollbackFunctions.clear();
        this.batchManager.clear();
        this.vertexCache.clear();
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The recordId is not referenced in the SqlgVertex.
     * It is important that the value of the WeakHashMap does not reference the key.
     *
     * @param sqlgGraph
     * @param recordId
     * @return
     */
    SqlgVertex putVertexIfAbsent(SqlgGraph sqlgGraph, RecordId recordId) {
        if (!this.vertexCache.containsKey(recordId)) {
            SqlgVertex sqlgVertex = new SqlgVertex(sqlgGraph, recordId.getId(), recordId.getSchemaTable().getSchema(), recordId.getSchemaTable().getTable());
            this.vertexCache.put(recordId, sqlgVertex);
            return sqlgVertex;
        } else {
            SqlgVertex sqlgVertex = this.vertexCache.get(recordId);
            if (sqlgVertex != null) {
                return sqlgVertex;
            } else {
                sqlgVertex = new SqlgVertex(sqlgGraph, recordId.getId(), recordId.getSchemaTable().getSchema(), recordId.getSchemaTable().getTable());
                this.vertexCache.put(recordId, sqlgVertex);
                return sqlgVertex;
            }
        }
    }

    SqlgVertex putVertexIfAbsent(SqlgVertex sqlgVertex) {
        if (!this.vertexCache.containsKey(sqlgVertex.id())) {
            //copy the RecordId so that the WeakHashMap value does not reference the key
            RecordId vertexRecordId = (RecordId) sqlgVertex.id();
            RecordId recordId = RecordId.from(SchemaTable.of(vertexRecordId.getSchemaTable().getSchema(), vertexRecordId.getSchemaTable().getTable()), vertexRecordId.getId());
            this.vertexCache.put(recordId, sqlgVertex);
            return sqlgVertex;
        } else {
            SqlgVertex sqlgVertexFromCache = this.vertexCache.get(sqlgVertex.id());
            if (sqlgVertexFromCache != null) {
                return sqlgVertexFromCache;
            } else {
                return sqlgVertex;
            }
        }
    }

    void add(SqlgVertex sqlgVertex) {
        if (this.vertexCache.containsKey(sqlgVertex.id())) {
            throw new IllegalStateException("The vertex cache should never already contain a new vertex!");
        } else {
            RecordId vertexRecordId = (RecordId) sqlgVertex.id();
            RecordId recordId = RecordId.from(SchemaTable.of(vertexRecordId.getSchemaTable().getSchema(), vertexRecordId.getSchemaTable().getTable()), vertexRecordId.getId());
            this.vertexCache.put(recordId, sqlgVertex);
        }
    }

}
