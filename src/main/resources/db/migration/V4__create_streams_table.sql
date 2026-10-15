CREATE TABLE streams (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         channel_id UUID NOT NULL,
                         category_id UUID,
                         title VARCHAR(255),
                         thumbnail_url VARCHAR(500),
                         ingest_url VARCHAR(500),
                         playback_url VARCHAR(500),
                         status VARCHAR(20) NOT NULL DEFAULT 'LIVE',
                         peak_viewers INTEGER NOT NULL DEFAULT 0,
                         avg_viewers INTEGER NOT NULL DEFAULT 0,
                         started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         ended_at TIMESTAMP,

                         CONSTRAINT fk_stream_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
                         CONSTRAINT fk_stream_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);