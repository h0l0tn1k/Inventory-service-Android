import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAnalysisValues, defaultValue } from 'app/shared/model/analysis-values.model';

export const ACTION_TYPES = {
  FETCH_ANALYSISVALUES_LIST: 'analysisValues/FETCH_ANALYSISVALUES_LIST',
  FETCH_ANALYSISVALUES: 'analysisValues/FETCH_ANALYSISVALUES',
  CREATE_ANALYSISVALUES: 'analysisValues/CREATE_ANALYSISVALUES',
  UPDATE_ANALYSISVALUES: 'analysisValues/UPDATE_ANALYSISVALUES',
  DELETE_ANALYSISVALUES: 'analysisValues/DELETE_ANALYSISVALUES',
  RESET: 'analysisValues/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAnalysisValues>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type AnalysisValuesState = Readonly<typeof initialState>;

// Reducer

export default (state: AnalysisValuesState = initialState, action): AnalysisValuesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ANALYSISVALUES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ANALYSISVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ANALYSISVALUES):
    case REQUEST(ACTION_TYPES.UPDATE_ANALYSISVALUES):
    case REQUEST(ACTION_TYPES.DELETE_ANALYSISVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ANALYSISVALUES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ANALYSISVALUES):
    case FAILURE(ACTION_TYPES.CREATE_ANALYSISVALUES):
    case FAILURE(ACTION_TYPES.UPDATE_ANALYSISVALUES):
    case FAILURE(ACTION_TYPES.DELETE_ANALYSISVALUES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANALYSISVALUES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANALYSISVALUES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ANALYSISVALUES):
    case SUCCESS(ACTION_TYPES.UPDATE_ANALYSISVALUES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ANALYSISVALUES):
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

const apiUrl = 'api/analysis-values';

// Actions

export const getEntities: ICrudGetAllAction<IAnalysisValues> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ANALYSISVALUES_LIST,
  payload: axios.get<IAnalysisValues>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IAnalysisValues> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ANALYSISVALUES,
    payload: axios.get<IAnalysisValues>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAnalysisValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ANALYSISVALUES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAnalysisValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ANALYSISVALUES,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAnalysisValues> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ANALYSISVALUES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
