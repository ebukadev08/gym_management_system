CREATE TABLE staff_profiles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  user_id UUID NOT NULL UNIQUE REFERENCES users(id),
  job_title VARCHAR(100),
  hired_at DATE
);

CREATE TABLE trainers (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  user_id UUID NOT NULL UNIQUE REFERENCES users(id),
  specialization VARCHAR(150),
  bio TEXT
);

CREATE TABLE members (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  user_id UUID NOT NULL UNIQUE REFERENCES users(id),
  phone VARCHAR(30),
  date_of_birth DATE,
  joined_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_staff_profiles_tenant ON staff_profiles(tenant_id);
CREATE INDEX idx_trainers_tenant ON trainers(tenant_id);
CREATE INDEX idx_members_tenant ON members(tenant_id);