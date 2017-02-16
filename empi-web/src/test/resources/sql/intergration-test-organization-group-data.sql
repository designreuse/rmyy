--用户
SET IDENTITY_INSERT sec_user ON;
insert into sec_user(id, user_name, email, mobile_phone_number, password, salt, create_date, status, is_admin, is_deleted, real_name) values
(1, 'admin', 'admin@jict.org', '18970986887', 'ec21fa1738f39d5312c6df46002d403d', 'yDd1956wn1', getdate(), 'normal', 'true', 'false', '管理员')
;
SET IDENTITY_INSERT sec_user OFF;

--组织机构
SET IDENTITY_INSERT sec_organization ON;
insert into sec_organization(id, parent_id, parent_ids, weight, name, is_show) values
(1, 0, '0/', 1, '组织机构', true)
,(2, 1, '0/1/', 1, '组织机构2', true)
,(3, 1, '0/', 1, '组织机构3', false)
;
SET IDENTITY_INSERT sec_organization OFF;

--用户--组织机构--工作职务关系
SET IDENTITY_INSERT sec_user_organization_job ON;
insert into sec_user_organization_job (id, user_id, organization_id, job_id) values
(1, 1, 2, null)
,(2, 1, 3, null)
;

insert into sec_group (id, name, type, is_show, default_group) values
(3, '组织机构组1', 'organization', true, false)
,(4, '组织机构组2', 'organization', false, false)
;

insert into sec_group_relation (id, group_id, organization_id) values
(3, 3, 1)
,(4, 3, 3)
,(5, 4, 2);
SET IDENTITY_INSERT sec_user_organization_job OFF;

--组织机构单独授权
SET IDENTITY_INSERT sec_auth ON;
insert into sec_auth (id, organization_id, job_id, user_id, group_id, role_ids, type) values
(1, 2, 0, 0, 0, '1', 'organization_job')

--组织机构组授权
,(4, 0, 0, 0, 3, '2,3', 'organization_group')
,(5, 0, 0, 0, 4, '1', 'organization_group')
;
SET IDENTITY_INSERT sec_auth OFF;