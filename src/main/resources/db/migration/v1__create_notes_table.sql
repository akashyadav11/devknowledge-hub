
-- ═══════════════════════════════════════════════════════
-- src/main/resources/db/migration/V1__create_notes_table.sql
-- ═══════════════════════════════════════════════════════

CREATE TABLE IF NOT EXISTS notes (
    id          VARCHAR(36)   PRIMARY KEY,
    title       VARCHAR(200)  NOT NULL,
    content     TEXT          NOT NULL,
    category    VARCHAR(100),
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS note_tags (
    note_id  VARCHAR(36)  NOT NULL REFERENCES notes(id) ON DELETE CASCADE,
    tag      VARCHAR(100) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_notes_category   ON notes(category);
CREATE INDEX IF NOT EXISTS idx_notes_created_at ON notes(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_note_tags_tag    ON note_tags(tag);