CREATE TABLE gym_classes (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  name VARCHAR(150) NOT NULL,
  description TEXT,
  capacity INT CHECK (capacity > 0),
  created_by UUID REFERENCES users(id)
);

CREATE TABLE class_sessions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  gym_class_id UUID NOT NULL REFERENCES gym_classes(id),
  starts_at TIMESTAMPTZ NOT NULL,
  ends_at TIMESTAMPTZ NOT NULL,
  location VARCHAR(150),
  status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
  CHECK (ends_at > starts_at)
);

CREATE TABLE trainer_assignments (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  trainer_id UUID NOT NULL REFERENCES trainers(id),
  class_session_id UUID NOT NULL REFERENCES class_sessions(id),
  assigned_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  unassigned_at TIMESTAMPTZ
);

-- A trainer can only be actively assigned to a session once.
-- Past (unassigned) rows are kept as history.
CREATE UNIQUE INDEX uq_active_trainer_assignment
  ON trainer_assignments(trainer_id, class_session_id)
  WHERE unassigned_at IS NULL;

CREATE TABLE class_registrations (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  member_id UUID NOT NULL REFERENCES members(id),
  class_session_id UUID NOT NULL REFERENCES class_sessions(id),
  status VARCHAR(20) NOT NULL DEFAULT 'REGISTERED',
  registered_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (member_id, class_session_id)
);

CREATE INDEX idx_gym_classes_tenant ON gym_classes(tenant_id);
CREATE INDEX idx_class_sessions_tenant ON class_sessions(tenant_id);
CREATE INDEX idx_class_sessions_class ON class_sessions(gym_class_id);
CREATE INDEX idx_trainer_assignments_tenant ON trainer_assignments(tenant_id);
CREATE INDEX idx_class_registrations_tenant ON class_registrations(tenant_id);