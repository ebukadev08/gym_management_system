-- Currencies
INSERT INTO currencies (code, name, symbol) VALUES
  ('NGN', 'Nigerian Naira', '₦'),
  ('USD', 'US Dollar', '$'),
  ('EUR', 'Euro', '€'),
  ('GBP', 'British Pound', '£'),
  ('AED', 'UAE Dirham', 'د.إ');

-- Fixed roles
INSERT INTO roles (name, description) VALUES
  ('SUPER_ADMIN', 'Platform-level administrator who manages all tenants'),
  ('ADMIN', 'Gym owner or administrator with full control of their gym'),
  ('MANAGER', 'Runs daily gym operations'),
  ('TRAINER', 'Delivers classes'),
  ('MEMBER', 'Gym member');

-- Permissions
INSERT INTO permissions (code, description) VALUES
  ('TENANT_MANAGE', 'Create and manage tenants'),
  ('STAFF_ONBOARD', 'Onboard new staff'),
  ('ROLE_ASSIGN', 'Assign roles to staff'),
  ('MEMBER_VIEW', 'View members'),
  ('MEMBER_MANAGE', 'Create and update members'),
  ('CLASS_CREATE', 'Create classes and sessions'),
  ('CLASS_VIEW', 'View classes and sessions'),
  ('TRAINER_ASSIGN', 'Assign and unassign trainers'),
  ('CLASS_REGISTER', 'Register for a class session'),
  ('EQUIPMENT_MANAGE', 'Manage equipment'),
  ('PLAN_MANAGE', 'Create and edit membership plans'),
  ('PLAN_VIEW', 'View membership plans'),
  ('SUBSCRIBE', 'Subscribe to a membership plan'),
  ('PAYMENT_MAKE', 'Make a payment'),
  ('PAYMENT_VIEW', 'View payments');

-- Role to permission links
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
JOIN permissions p ON p.code IN ('TENANT_MANAGE')
WHERE r.name = 'SUPER_ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
JOIN permissions p ON p.code IN (
  'STAFF_ONBOARD','ROLE_ASSIGN','MEMBER_VIEW','MEMBER_MANAGE','CLASS_CREATE',
  'CLASS_VIEW','TRAINER_ASSIGN','EQUIPMENT_MANAGE','PLAN_MANAGE','PLAN_VIEW','PAYMENT_VIEW')
WHERE r.name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
JOIN permissions p ON p.code IN (
  'STAFF_ONBOARD','ROLE_ASSIGN','MEMBER_VIEW','CLASS_CREATE','CLASS_VIEW',
  'TRAINER_ASSIGN','EQUIPMENT_MANAGE','PLAN_VIEW')
WHERE r.name = 'MANAGER';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
JOIN permissions p ON p.code IN ('CLASS_VIEW')
WHERE r.name = 'TRAINER';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
JOIN permissions p ON p.code IN (
  'CLASS_VIEW','CLASS_REGISTER','PLAN_VIEW','SUBSCRIBE','PAYMENT_MAKE')
WHERE r.name = 'MEMBER';