package com.rx.misc;


import com.rx.dao.DocumentType;

public class DocumentTypeResolver {
    public static DocumentType resolve(DocumentRootType documentRootType,
                                       DocumentSubType documentSubType) {
        for (DocumentType documentType: DocumentType.values()) {
            if (documentRootType == DocumentRootType.resolve(documentType)
                    && documentSubType == DocumentSubType.resolve(documentType)) {
                return documentType;
            }
        }

        return null;
    }
}
