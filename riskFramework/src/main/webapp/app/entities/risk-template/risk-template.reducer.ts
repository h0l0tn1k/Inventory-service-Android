import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRiskTemplate, defaultValue } from 'app/shared/model/risk-template.model';

export const ACTION_TYPES = {
  FETCH_RISKTEMPLATE_LIST: 'riskTemplate/FETCH_RISKTEMPLATE_LIST',
  FETCH_RISKTEMPLATE: 'riskTemplate/FETCH_RISKTEMPLATE',
  CREATE_RISKTEMPLATE: 'riskTemplate/CREATE_RISKTEMPLATE',
  UPDATE_RISKTEMPLATE: 'riskTemplate/UPDATE_RISKTEMPLATE',
  DELETE_RISKTEMPLATE: 'riskTemplate/DELETE_RISKTEMPLATE',
  RESET: 'riskTemplate/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRiskTemplate>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type RiskTemplateState = Readonly<typeof initialState>;

// Reducer

export default (state: RiskTemplateState = initialState, action): RiskTemplateState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RISKTEMPLATE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RISKTEMPLATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_RISKTEMPLATE):
    case REQUEST(ACTION_TYPES.UPDATE_RISKTEMPLATE):
    case REQUEST(ACTION_TYPES.DELETE_RISKTEMPLATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_RISKTEMPLATE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RISKTEMPLATE):
    case FAILURE(ACTION_TYPES.CREATE_RISKTEMPLATE):
    case FAILURE(ACTION_TYPES.UPDATE_RISKTEMPLATE):
    case FAILURE(ACTION_TYPES.DELETE_RISKTEMPLATE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_RISKTEMPLATE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_RISKTEMPLATE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_RISKTEMPLATE):
    case SUCCESS(ACTION_TYPES.UPDATE_RISKTEMPLATE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_RISKTEMPLATE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/risk-templates';

// Actions

export const getEntities: ICrudGetAllAction<IRiskTemplate> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RISKTEMPLATE_LIST,
  payload: axios.get<IRiskTemplate>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IRiskTemplate> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RISKTEMPLATE,
    payload: axios.get<IRiskTemplate>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRiskTemplate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RISKTEMPLATE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRiskTemplate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RISKTEMPLATE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRiskTemplate> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RISKTEMPLATE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
