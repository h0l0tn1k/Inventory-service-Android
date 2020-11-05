import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/status-code">
      <Translate contentKey="global.menu.entities.statusCode" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/risk">
      <Translate contentKey="global.menu.entities.risk" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/risk-assessment">
      <Translate contentKey="global.menu.entities.riskAssessment" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/analysis-values">
      <Translate contentKey="global.menu.entities.analysisValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/risk-template">
      <Translate contentKey="global.menu.entities.riskTemplate" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
