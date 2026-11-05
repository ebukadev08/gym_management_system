CREATE TABLE equipment_categories (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  name VARCHAR(100) NOT NULL
);

CREATE TABLE equipment (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  category_id UUID REFERENCES equipment_categories(id),
  name VARCHAR(150) NOT NULL,
  serial_number VARCHAR(100),
  status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
  purchased_at DATE
);

CREATE TABLE translations (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  entity_type VARCHAR(50) NOT NULL,
  entity_id UUID NOT NULL,
  field_name VARCHAR(50) NOT NULL,
  language_code VARCHAR(5) NOT NULL REFERENCES languages(code),
  value TEXT NOT NULL,
  UNIQUE (entity_type, entity_id, field_name, language_code)
);

CREATE INDEX idx_equipment_categories_tenant ON equipment_categories(tenant_id);
CREATE INDEX idx_equipment_tenant ON equipment(tenant_id);
CREATE INDEX idx_translations_tenant ON translations(tenant_id);