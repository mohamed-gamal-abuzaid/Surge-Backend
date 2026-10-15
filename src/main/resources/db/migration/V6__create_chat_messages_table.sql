CREATE TABLE chat_messages (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               stream_id UUID NOT NULL,
                               user_id UUID NOT NULL,
                               content TEXT NOT NULL,
                               type VARCHAR(20) NOT NULL DEFAULT 'TEXT',
                               is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                               sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_chat_stream FOREIGN KEY (stream_id) REFERENCES streams(id) ON DELETE CASCADE,
                               CONSTRAINT fk_chat_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_chat_stream_sent ON chat_messages(stream_id, sent_at);